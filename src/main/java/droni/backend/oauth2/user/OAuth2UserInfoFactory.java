package droni.backend.oauth2.user;

import droni.backend.oauth2.execption.OAuth2ProviderNotFoundException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String, Object> attributes) {
        OAuth2ProviderEnum oAuth2ProviderEnum = OAuth2ProviderEnum.fromRegistrationId(registrationId);
        OAuth2UserInfo oAuth2UserInfo = switch (oAuth2ProviderEnum) {
            case NAVER -> new NaverOAuth2UserInfo(accessToken, attributes);
            default -> throw new OAuth2ProviderNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, "Not Supported OAuth2 User");
        };
        return oAuth2UserInfo;
    }
}
