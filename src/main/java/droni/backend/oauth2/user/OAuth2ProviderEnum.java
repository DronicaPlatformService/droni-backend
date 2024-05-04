package droni.backend.oauth2.user;

import droni.backend.oauth2.execption.OAuth2ProviderNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuth2ProviderEnum {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String registrationId;

    public static OAuth2ProviderEnum fromRegistrationId(String registrationId) {
        return Arrays.stream(OAuth2ProviderEnum.values()).parallel()
                .filter(oAuth2ProviderEnum -> oAuth2ProviderEnum.getRegistrationId().equals(registrationId))
                .findFirst()
                .orElseThrow(() -> new OAuth2ProviderNotFoundException(HttpStatus.INTERNAL_SERVER_ERROR, String.format("Provider %s not found", registrationId)));
    }

}
