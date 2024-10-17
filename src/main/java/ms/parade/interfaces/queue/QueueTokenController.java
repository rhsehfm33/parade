package ms.parade.interfaces.queue;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueTokenInfo;
import ms.parade.domain.queue.QueueTokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/queue-tokens")
public class QueueTokenController {
    private final QueueTokenService queueTokenService;

    @GetMapping("/{uuid}")
    ResponseEntity<QueueTokenResponse> getQueueToken(@PathVariable long uuid) {
        QueueTokenInfo queueTokenInfo = queueTokenService.findById(uuid);
        QueueTokenResponse queueTokenResponse = new QueueTokenResponse(queueTokenInfo);
        return ResponseEntity.ok(queueTokenResponse);
    }

    @PutMapping("/{userId}")
    ResponseEntity<QueueTokenResponse> putQueueToken(@PathVariable long userId) {
        QueueTokenInfo queueTokenInfo = queueTokenService.putUnique(userId);
        QueueTokenResponse queueTokenResponse = new QueueTokenResponse(queueTokenInfo);
        return ResponseEntity.ok(queueTokenResponse);
    }

}
