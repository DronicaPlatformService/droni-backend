package droni.backend.oauth2.user.impl;

import droni.backend.oauth2.user.OAuth2UserInfo;
import droni.backend.oauth2.user.ProviderType;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attribute;
    private final String accessToken;
    private final Long id;
    private final String nickname;
    // todo : 현재는 kakao 인증이 없어서 email 을 테스트로 생성
    private final String email;
    private final String profileImageUrl;



    @SuppressWarnings("all")
    public KakaoOAuth2UserInfo(String accessToken, Map<String, Object> attribute) {
        this.accessToken = accessToken;
        this.attribute = attribute;
        this.id = (Long) attribute.get("id");
        Map<String, Object> kakaoAccountMap = (Map<String, Object>) attribute.get("properties");
        this.nickname = (String) kakaoAccountMap.get("nickname");
        this.email = this.nickname + "test@kakao.com";
        this.profileImageUrl = (String) kakaoAccountMap.get("profile_image");
    }
    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getProviderAccessToken() {
        return this.accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attribute;
    }

    @Override
    public String getOauth2Id() {
        return String.valueOf(this.id);
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getFirstName() {
        return null;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    @Override
    public String getProfileImageUrl() {
        return this.profileImageUrl;
    }
}
