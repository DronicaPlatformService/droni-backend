package droni.backend.global.exception;

import org.springframework.http.HttpStatus;

public class DroniBadRequestException extends BaseException{
    public DroniBadRequestException(HttpStatus httpStatus, String msg) {
        super(httpStatus, msg);
    }
}
