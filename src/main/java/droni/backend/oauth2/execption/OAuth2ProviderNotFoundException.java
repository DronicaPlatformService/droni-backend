package droni.backend.oauth2.execption;

import droni.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class OAuth2ProviderNotFoundException extends AuthenticationException {
    public OAuth2ProviderNotFoundException(HttpStatus httpStatus, String msg) {
        super(httpStatus, msg);
    }
}
