package ms.parade.integration.domain.point;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ms.parade.domain.point.PointType;
import ms.parade.domain.point.UserPointRepository;
import ms.parade.domain.point.UserPointService;
import ms.parade.infrastructure.point.UserPointParams;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // test 메서드마다 db reset
public class UserPointServiceWholeTest {
    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private UserPointService userPointService;

    private final int THREAD_COUNT = 100;
    private final int CHARGE_AMOUNT = 100;
    private final int SPEND_AMOUNT = 10;

    private Callable<Void> getCallableOfChangeUserPoint(long userId, long amount, PointType type) {
        return (() -> {
            userPointService.changeUserPoint(userId, amount, type);
            return null;
        });
    }

    @Test
    public void changeUserPoint_MultipleCharges_AllCharged() throws InterruptedException {
        userPointRepository.save(new UserPointParams(1, 0));

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; ++i) {
            tasks.add(getCallableOfChangeUserPoint(1, CHARGE_AMOUNT, PointType.CHARGE));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.invokeAll(tasks);

        assertEquals(THREAD_COUNT * CHARGE_AMOUNT, userPointRepository.findByUserId(1).get().point());
    }

    @Test
    public void changeUserPoint_MultipleSpends_AllSpent() throws InterruptedException {
        userPointRepository.save(new UserPointParams(1, THREAD_COUNT * SPEND_AMOUNT));

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; ++i) {
            tasks.add(getCallableOfChangeUserPoint(1, SPEND_AMOUNT, PointType.SPEND));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        executorService.invokeAll(tasks);

        assertEquals(0, userPointRepository.findByUserId(1).get().point());
    }

    @Test
    public void changeUserPoint_SpendsAndCharges_AllApplied() throws InterruptedException {
        userPointRepository.save(new UserPointParams(1, THREAD_COUNT * CHARGE_AMOUNT));

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; ++i) {
            tasks.add(getCallableOfChangeUserPoint(1, CHARGE_AMOUNT, PointType.CHARGE));
        }
        for (int i = 0; i < THREAD_COUNT; ++i) {
            tasks.add(getCallableOfChangeUserPoint(1, SPEND_AMOUNT, PointType.SPEND));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT * 2);
        executorService.invokeAll(tasks);

        long expected = THREAD_COUNT * CHARGE_AMOUNT * 2 - THREAD_COUNT * SPEND_AMOUNT;
        assertEquals(expected, userPointRepository.findByUserId(1).get().point());
    }
}
