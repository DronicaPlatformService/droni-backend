package droni.backend.oauth2.execption;

public class TokenValidFailedException extends AuthenticationException {
    public TokenValidFailedException(String msg) {
        super(msg);
    }
}
