package ms.parade.integration.application.reservation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ms.parade.application.reservation.ReservationFacade;
import ms.parade.domain.concert.ConcertRepository;
import ms.parade.domain.concert.ConcertSchedule;
import ms.parade.domain.reservation.SeatReservationRepository;
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
    private SeatReservationRepository seatReservationRepository;

    @Autowired
    private ReservationFacade reservationFacade;

    @Test
    public void reserveSeat_5TimesAtOnce_OnlyOneReservation() throws InterruptedException {
        final int THREAD_COUNT = 5;

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
        ConcertSchedule concertSchedule = concertRepository.saveSchedule(concertScheduleParams);

        // 데이터 세팅 완료
        // 테스트 준비
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(THREAD_COUNT);

        // 테스트 성공, 실패 카운트 기록
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 1; i <= THREAD_COUNT; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    reservationFacade.reserveSeat(userId, seat.id());
                    successCount.incrementAndGet();
                } catch (IllegalStateException e) {
                    failureCount.incrementAndGet();
                    System.err.println(e);
                } catch (InterruptedException e) {
                    System.err.println(e);
                } finally {
                    endLatch.countDown();
                }
            });
            Thread.sleep(20);
        }

        readyLatch.await();
        startLatch.countDown(); // 테스트 시작
        endLatch.await();
        executorService.shutdown();

        assertEquals(1, successCount.get());
        assertEquals(4, failureCount.get());
    }
}
