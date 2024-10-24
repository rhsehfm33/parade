package ms.parade.interfaces.common.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ms.parade.interfaces.common.util.TraceIdHolder;

public class TraceIdFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // 요청 시작 시간 기록
        long startTime = System.currentTimeMillis();

        // trace_id 생성하고, 요청 & 응답 & thread local에 저장
        String traceId = UUID.randomUUID().toString();
        TraceIdHolder.setTraceId(traceId);
        request.setAttribute(TRACE_ID_HEADER, traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);

        String contentType = request.getContentType();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String clientIP = request.getRemoteAddr();

        try {
            // 요청 정보 로깅 (요청 처리 전)
            logger.info("\nRequest - URI: {}, method: {}, contentType: {}, client IP: {}, startTime: {},"
                + " Trace ID: {}\n", requestURI, method, contentType, clientIP, startTime, traceId);

            // 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // 응답 정보 로깅 (응답 처리 후)
            logger.info("\nResponse - URI: {}, Status: {}, contentType:{}, duration: {}ms, Trace ID: {}\n",
                requestURI, response.getStatus(), contentType, duration, traceId);

            TraceIdHolder.clear();
        }
    }
}
