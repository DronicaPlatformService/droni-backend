package droni.backend.global.exception;

import org.springframework.http.HttpStatus;

public class DroniServerException extends BaseException{
    public DroniServerException(HttpStatus httpStatus, String msg) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, msg);
    }
}
