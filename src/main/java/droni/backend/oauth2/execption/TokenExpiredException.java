package droni.backend.oauth2.execption;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends AuthenticationException {
    public TokenExpiredException(String msg) {
        super(HttpStatus.FORBIDDEN, "JWT is expired");
    }
}
