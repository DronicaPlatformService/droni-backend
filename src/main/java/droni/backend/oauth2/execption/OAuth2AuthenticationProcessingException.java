package droni.backend.oauth2.execption;

public class OAuth2AuthenticationProcessingException extends AuthenticationException {

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
