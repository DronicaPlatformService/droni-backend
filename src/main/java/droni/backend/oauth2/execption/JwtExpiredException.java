package droni.backend.oauth2.execption;

import org.springframework.http.HttpStatus;

public class JwtExpiredException extends JWTException{
    public JwtExpiredException(String msg) {
        super(msg);
    }

    @Override
    public int getStatus() {
        return HttpStatus.NOT_ACCEPTABLE.value();
    }
}
