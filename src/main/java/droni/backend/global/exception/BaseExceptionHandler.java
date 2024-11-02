package droni.backend.global.exception;

import droni.backend.global.dto.DrnErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Order
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    public BaseExceptionHandler() {
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<DrnErrorResponse> handleBaseException(BaseException e, HttpServletRequest request) {
        log.warn("Exception in Web Request : {}", e.getMessage());
        return this.createErrorResponse(e, request, e.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<DrnErrorResponse> handleRuntimeException(Exception e, HttpServletRequest request) {

        Throwable rootCause = ExceptionUtils.getRootCause(e);
        StackTraceElement rootSource = rootCause.getStackTrace()[0];
        log.error("Exception in Web Request : {} {} \n QueryParams: {} \n Exception: {}:{} \n Location:{}:{}.{}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString(),
                rootCause.getClass().getSimpleName(),
                rootCause.getMessage(),
                rootSource.getClassName(),
                rootSource.getMethodName(),
                rootSource.getLineNumber()
                );
        return this.createErrorResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<DrnErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return this.createErrorResponse(e, request, HttpStatus.BAD_REQUEST);
    }


    protected ResponseEntity<DrnErrorResponse> createErrorResponse(Throwable e, HttpServletRequest request, HttpStatus httpStatus) {
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        DrnErrorResponse responseBody = DrnErrorResponse.builder()
                .httpStatus(httpStatus)
                .error(e.getMessage())
                .exception(e.getClass().getSimpleName())
                .path(request.getRequestURI())
                .queryString(request.getQueryString())
                .pathVariable(pathVariables)
                .parameterMap(request.getParameterMap())
                .build();

        return new ResponseEntity<>(responseBody, responseBody.getHttpStatus());
    }
}
