package ms.parade.unit.domain.queue;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ms.parade.domain.queue.QueueToken;
import ms.parade.domain.queue.QueueTokenInfo;
import ms.parade.domain.queue.QueueTokenRepository;
import ms.parade.domain.queue.QueueTokenService;

@ExtendWith(MockitoExtension.class)
public class QueueTokenServiceTest {
    @Mock
    QueueTokenRepository queueTokenRepository;

    @InjectMocks
    QueueTokenService queueTokenService;

    @Test
    public void putUnique_AlreadyExists_IllegalArgumentException() {
        QueueToken queueToken = new QueueToken(1, 1, null, null, null);
        when(queueTokenRepository.findByUserId(1)).thenReturn(Optional.of(queueToken));
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> queueTokenService.putUnique(1));
    }

    @Test
    public void findFrontWaits_2QueueTokens_OrderOf1And2() {
        QueueToken queueToken1 = new QueueToken(1, 1, null, null, null);
        QueueToken queueToken2 = new QueueToken(2, 1, null, null, null);
        List<QueueToken> queueTokens = List.of(queueToken1, queueToken2);
        when(queueTokenRepository.findFrontWaits()).thenReturn(queueTokens);

        List<QueueTokenInfo> queueTokenInfos = queueTokenService.findFrontWaits();
        Assertions.assertEquals(2, queueTokenInfos.size());
        Assertions.assertEquals(1, queueTokenInfos.get(0).order());
        Assertions.assertEquals(2, queueTokenInfos.get(1).order());
    }
}
