package ms.parade.interfaces.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ms.parade.interfaces.common.util.TraceIdHolder;

@Aspect
@Component
public class ExecutionLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(ExecutionLoggingAspect.class);

    // service 클래스에 로깅 적용
    @Pointcut("execution(* ms.parade.domain..*Service.*(..))")
    public void domainService() {
    }

    // trace_id 및 실행 시간 로깅
    @Around("domainService()")
    public Object logExecutionInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        // trace_id 설정 (없을 경우 새로 생성)
        long startTime = System.currentTimeMillis(); // 실행 시작 시간
        String fullClassName = joinPoint.getSignature().getDeclaringTypeName();
        String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String simpleMethodName = joinPoint.getSignature().getName();

        try {
            // 메소드 실행
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();  // 실행 종료 시간
            long executionTime = endTime - startTime;   // 총 실행 시간 계산

            // 메소드 완료 후 로그
            logger.info("\nCompleted - class: {}, method: {}, trace_id: {}, Execution Time: {} ms\n",
                simpleClassName, simpleMethodName, TraceIdHolder.getTraceId(), executionTime);
        }
    }
}
