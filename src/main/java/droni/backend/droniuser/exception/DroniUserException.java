package droni.backend.droniuser.exception;

import droni.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DroniUserException extends BaseException {
    public DroniUserException(HttpStatus httpStatus, String msg) {
        super(httpStatus, msg);
    }
}
