package droni.backend.oauth2.service;

import droni.backend.droniuser.entity.DroniUser;
import droni.backend.oauth2.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2UserPrincipal implements OAuth2User, UserDetails {
    private final OAuth2UserInfo userInfo;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userInfo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return userInfo.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return userInfo.getAttributes();
    }

    public String getOAuth2Id() {
        return this.userInfo.getId();
    }

    public DroniUser newDroniUserFromPrincipal(String refreshToken) {
        return DroniUser.builder()
                .name(this.getName())
                .email(userInfo.getEmail())
                .phoneNumber("EMPTY_NUMBER")
                .profileImage(userInfo.getProfileImageUrl())
                .oauthId(this.getOAuth2Id())
                .provider(userInfo.getProvider())
                .nickname(userInfo.getNickname())
                .timeZone("Asia/Seoul")
                .refreshToken(refreshToken)
                .notificationEnabled(true)
                .marketingEnabled(true)
                .build();
    }
}
