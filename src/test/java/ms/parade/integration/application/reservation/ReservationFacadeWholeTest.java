package ms.parade.integration.application.reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ms.parade.application.reservation.ReservationFacade;
import ms.parade.domain.concert.ConcertRepository;
import ms.parade.domain.seat.Seat;
import ms.parade.domain.seat.SeatRepository;
import ms.parade.domain.seat.SeatStatus;
import ms.parade.domain.user.UserRepository;
import ms.parade.infrastructure.concert.ConcertScheduleParams;
import ms.parade.infrastructure.seat.SeatParams;
import ms.parade.infrastructure.user.UserParams;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // test 메서드마다 db reset
public class ReservationFacadeWholeTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ReservationFacade reservationFacade;

    @Test
    public void reserveSeat_100TimesAtOnce_OnlyOneReservation() throws InterruptedException {
        final int THREAD_COUNT = 100;

        // 사용자 데이터 설정
        for (int i = 1; i <= THREAD_COUNT; i++) {
            UserParams userParams = new UserParams("user" + i + "@gmail.com", "test");
            userRepository.save(userParams);
        }

        // 좌석 데이터 설정
        SeatParams seatParams = new SeatParams(1, "A1", 100_000, SeatStatus.EMPTY);
        Seat seat = seatRepository.save(seatParams);

        // 스케쥴 데이터 설정
        ConcertScheduleParams concertScheduleParams = new ConcertScheduleParams(
            1, 50, 50, LocalDate.now());
        concertRepository.saveSchedule(concertScheduleParams);

        // 데이터 세팅 완료

        // 테스트 성공, 실패 카운트 기록
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 1; i <= THREAD_COUNT; i++) {
            long userId = i;
            tasks.add(() -> {
                try {
                    reservationFacade.reserveSeat(userId, seat.id());
                    successCount.incrementAndGet();
                } catch (IllegalStateException e) {
                    failureCount.incrementAndGet();
                    System.err.println(e);
                }
                return null;
            });
        }
        // 모든 작업을 동시에 실행하고 모든 작업이 완료될 때까지 대기
        executorService.invokeAll(tasks);

        // 결과 검증
        assertEquals(1, successCount.get());
        assertEquals(THREAD_COUNT - 1, failureCount.get());
    }
}
