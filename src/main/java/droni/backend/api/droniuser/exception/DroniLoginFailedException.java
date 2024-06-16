package droni.backend.api.droniuser.exception;

import droni.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DroniLoginFailedException extends BaseException {
    public DroniLoginFailedException(HttpStatus httpStatus, String msg) {
        super(httpStatus, msg);
    }
}
