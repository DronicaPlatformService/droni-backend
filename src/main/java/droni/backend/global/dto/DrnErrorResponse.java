package droni.backend.global.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Builder
@Getter
public class DrnErrorResponse {
    private Timestamp timestamp;
    private HttpStatus httpStatus;
    private String error;
    private String path;
    private String exception;
    private String queryString;
    private Map<String, String> pathVariable;
    private Map<String, String[]> parameterMap;

    public DrnErrorResponse(HttpStatus httpStatus, String error, String exception, String path, String queryString, Map<String, String[]> parameterMap, Map<String, String> pathVariable) {
        this.timestamp = new Timestamp((new Date()).getTime());
        this.httpStatus = httpStatus;
        this.error = error;
        this.exception = exception;
        this.path = path;
        this.queryString = queryString;
        this.pathVariable = pathVariable;
        this.parameterMap = parameterMap;
    }

    public static class DrnErrorResponseBuilder{
        private HttpStatus httpStatus;
        private String error;
        private String exception;
        private String path;
        private String queryString;
        private Map<String, String[]> parameterMap;
        private Map<String, String> pathVariable;

        public DrnErrorResponse build() {
            return new DrnErrorResponse(this.httpStatus, this.error, this.exception, this.path, this.queryString, this.parameterMap, this.pathVariable);
        }

    }
}
