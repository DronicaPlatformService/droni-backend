package droni.backend.config.websocket;

import droni.backend.global.exception.BaseException;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Data
public class StompErrorResponse {
    private int status;
    private String message;

    public StompErrorResponse(BaseException exception) {
        this.status = exception.getHttpStatus().value();
        this.message = exception.getMessage();
    }
}
