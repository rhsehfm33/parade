package ms.parade.interfaces.common.interceptor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ms.parade.domain.queue.QueueTokenInfo;
import ms.parade.domain.queue.QueueTokenService;
import ms.parade.domain.queue.QueueTokenStatus;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final QueueTokenService queueTokenService;

    @Override
    public boolean preHandle(
        HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler
    ) throws IllegalArgumentException {
        try {
            String uuid = request.getHeader("Authorization");
            QueueTokenInfo queueTokenInfo = queueTokenService.getById(uuid);

            if (!QueueTokenStatus.PASS.equals(queueTokenInfo.queueToken().status())) {
                throw new IllegalArgumentException("아직 대기중입니다.");
            }

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(queueTokenInfo.queueToken().userId(), null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}