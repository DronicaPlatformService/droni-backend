package droni.backend.oauth2.user;

import java.util.Map;

public interface OAuth2UserInfo {
    ProviderType getProvider();

    String getAccessToken();

    Map<String, Object> getAttributes();

    String getOauth2Id();

    String getEmail();

    String getName();

    String getFirstName();

    String getLastName();

    String getNickname();

    String getProfileImageUrl();
}
