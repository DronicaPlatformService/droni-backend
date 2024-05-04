package droni.backend.global.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private HttpStatus httpStatus;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
