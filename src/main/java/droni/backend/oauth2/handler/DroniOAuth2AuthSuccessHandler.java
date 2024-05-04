package droni.backend.oauth2.handler;

import droni.backend.config.properties.AppAuthProperties;
import droni.backend.droniuser.entity.DroniUser;
import droni.backend.droniuser.repository.DroniUserRepository;
import droni.backend.oauth2.DroniCookieAuthorizationRequestRepository;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import droni.backend.oauth2.util.DroniCookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static droni.backend.oauth2.DroniCookieAuthorizationRequestRepository.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class DroniOAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final DroniCookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final AppAuthProperties authProperties;
    private final AuthTokenProvider tokenProvider;
    private final DroniUserRepository userRepository;
    private final String defaultTargetUrl = "/";

    /**
     * 유저가 oauth2 인증에 성공했을 때 -> access token 발급해서 요청했던 페이지에 실어서 redirect
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = this.determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        }

        this.clearAuthenticationAttributes(request, response);
        super.getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    /**
     * 로그인 시에 토큰 발급 후에 redirect uri를 반환하는 메소드
     */
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUrlString = DroniCookieUtils.getCookie(request, "redirect_url")
                .map(Cookie::getValue)
                .orElse(defaultTargetUrl);
        OAuth2UserPrincipal oAuth2UserPrincipal = this.getOAuth2UserPrincipal(authentication);

        if (Objects.isNull(oAuth2UserPrincipal)) {
            return UriComponentsBuilder.fromUriString(redirectUrlString).queryParam("error", "Login failed").build().toUriString();
        }

        Date now = new Date();
        long accessTokenExpiry = now.getTime() + authProperties.getAuth().getTokenExpiry();
        AuthToken accessToken = tokenProvider.createAuthToken(oAuth2UserPrincipal.getName(), new Date(accessTokenExpiry));

        long refreshTokenExpiry = now.getTime() + authProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(authProperties.getAuth().getTokenSecret(), new Date(refreshTokenExpiry));

        this.authenticateOrRegisterUser(oAuth2UserPrincipal, refreshToken);

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        DroniCookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        DroniCookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);


        return UriComponentsBuilder.fromUriString(redirectUrlString)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();



    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }


    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

    }

    private void authenticateOrRegisterUser(OAuth2UserPrincipal userPrincipal, AuthToken refreshToken) {
        Optional<DroniUser> optionalDroniUser = userRepository.findDroniUserByOauth2id(userPrincipal.getOAuth2Id());
        if (optionalDroniUser.isPresent()) {
            DroniUser droniUser = optionalDroniUser.get();
            droniUser.updateRefreshToken(refreshToken.getToken());
        } else {
            DroniUser droniUser = userPrincipal.newDroniUserFromPrincipal(refreshToken.getToken());
            userRepository.save(droniUser);
        }

    }


}
