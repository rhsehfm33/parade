package ms.parade.interfaces.point;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ms.parade.application.point.PointFacade;
import ms.parade.application.point.UserPointResult;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class PointController {
    private final PointFacade pointFacade;

    @GetMapping("/protected/users/point")
    ResponseEntity<UserPointResponse> getUserPoint(Authentication authentication) {
        long userId = (long)authentication.getPrincipal();
        long point = pointFacade.getUserPoint(userId);
        return ResponseEntity.ok(new UserPointResponse(userId, point));
    }

    @PostMapping("/protected/users/{userId}/point")
    ResponseEntity<UserPointResponse> chargeUserPoint(
        Authentication authentication, @RequestBody UserPointRequest userPointRequest
    ) {
        long userId = (long)authentication.getPrincipal();
        UserPointResult userPointResult = pointFacade.chargePoint(userId, userPointRequest.amount());
        return ResponseEntity.ok(new UserPointResponse(userId, userPointResult.user().point()));
    }
}
