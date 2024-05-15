package droni.backend.oauth2.execption;

import org.springframework.http.HttpStatus;

public class JWTException extends AuthenticationException {
    public JWTException(String msg) {
        super(HttpStatus.FORBIDDEN, msg);
    }
}
