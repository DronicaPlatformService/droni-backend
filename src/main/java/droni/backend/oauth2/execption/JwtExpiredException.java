package droni.backend.oauth2.execption;

public class JwtExpiredException extends JWTException{
    public JwtExpiredException(String msg) {
        super(msg);
    }
}
