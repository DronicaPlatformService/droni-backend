package droni.backend.global.exception;

import droni.backend.global.dto.DrnErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler({BaseException.class})
    protected ResponseEntity<DrnErrorResponse> handleBaseException(BaseException e, HttpServletRequest request) {
        log.error("Exception in Web Request", e);
        return this.createErrorResponse(e, request, e.getHttpStatus());
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    protected ResponseEntity<DrnErrorResponse> handleRuntimeException(Exception e, HttpServletRequest request) {
        log.error("Exception in Web Request", e);
        return this.createErrorResponse(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
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

        return new ResponseEntity(responseBody, responseBody.getHttpStatus());
    }
}
