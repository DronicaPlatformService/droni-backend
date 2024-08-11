package droni.backend.oauth2.service;

import droni.backend.oauth2.execption.AuthenticationException;
import droni.backend.oauth2.execption.OAuth2AuthenticationProcessingException;
import droni.backend.oauth2.user.OAuth2UserInfo;
import droni.backend.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * OAuth2LoginAuthenticationFilter 에서 해당 클래스의 load user가 호출된다.
 * oauth2 서버에서 받은 access token 으로 resource server 에서 정보를 받은 후 가공하는 서비스
 */
@Service
public class DroniOAuthUserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // DefaultService에서 accesstoken 을 사용해서 provider의 resource server에서 User를 받아온다.
        OAuth2User OAuth2User = super.loadUser(userRequest);
        try {
            return this.processOAuth2User(userRequest, OAuth2User);
        } catch (AuthenticationException exception) {
            throw exception;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        OAuth2UserInfo OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, accessToken, oAuth2User.getAttributes());

        if (!StringUtils.hasText(OAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        return new OAuth2UserPrincipal(OAuth2UserInfo);



    }
}

