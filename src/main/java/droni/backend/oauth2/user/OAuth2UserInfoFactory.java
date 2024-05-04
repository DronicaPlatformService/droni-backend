package droni.backend.oauth2.user;

import droni.backend.oauth2.execption.OAuth2ProviderNotFoundException;
import droni.backend.oauth2.user.impl.NaverOAuth2UserInfo;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String, Object> attributes) {
        ProviderType providerType = ProviderType.fromRegistrationId(registrationId);
        OAuth2UserInfo oAuth2UserInfo = switch (providerType) {
            case NAVER -> new NaverOAuth2UserInfo(accessToken, attributes);
            default -> throw new OAuth2ProviderNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, "Not Supported OAuth2 User");
        };
        return oAuth2UserInfo;
    }
}
