package ms.parade.interfaces.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import ms.parade.interfaces.common.util.TraceIdHolder;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 예외 문구를 ';' 토큰으로 분리해 errorType, errorMessage 추출. <br>
     * 이 때 문자열 한 개면 errorMessage 로 설정,
     * 두 개면 errorType, errorMessage 로 설정.
     * 이 외의 경우는 ErrorResponse 설정 오류 반환.
     *
     * @param exceptionMessage 예외 메시지
     * @param status 반환할 HTTP 상태 코드
     * @return ResponseEntity with ErrorResponse body
     */
    private ResponseEntity<ErrorResponse> toErrorResponseEntity(String exceptionMessage, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(null, null);
        HttpStatus finalStatus = status;

        // 잘못된 형식의 예외 메시지가 전달됐을 경우, 형식 오류라고 반환
        if (exceptionMessage == null || exceptionMessage.trim().isEmpty() ||
            exceptionMessage.split(";").length == 0 || exceptionMessage.split(";").length > 2) {
            errorResponse = new ErrorResponse("WRONG_ERROR_RESPONSE", "Invalid error response format");
            finalStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String[] errorInfo = exceptionMessage.split(";");
        if (errorInfo.length == 1) {
            errorResponse = new ErrorResponse(null, errorInfo[0].trim());
        } else if (errorInfo.length == 2) {
            errorResponse = new ErrorResponse(errorInfo[0].trim(), errorInfo[1].trim());
        }

        // 오류 내역 로깅
        logger.error("\nError Type: {}, Error Message: {}, Status: {}, Trace ID : {}\n",
            errorResponse.errorType(), errorResponse.errorMessage(), finalStatus, TraceIdHolder.getTraceId()
        );

        return new ResponseEntity<>(errorResponse, finalStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return toErrorResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
