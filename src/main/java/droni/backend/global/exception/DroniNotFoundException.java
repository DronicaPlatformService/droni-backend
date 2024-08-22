package droni.backend.global.exception;

import org.springframework.http.HttpStatus;

public class DroniNotFoundException extends BaseException {

    public DroniNotFoundException(HttpStatus httpStatus, String msg) {
        super(httpStatus, msg);
    }
}
