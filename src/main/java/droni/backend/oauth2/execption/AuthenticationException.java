package droni.backend.oauth2.execption;

import droni.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public abstract class AuthenticationException extends BaseException {
    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(HttpStatus httpStatus, String msg) {
        super(httpStatus, msg);
    }
}
