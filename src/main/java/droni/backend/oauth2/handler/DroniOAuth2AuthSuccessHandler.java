package droni.backend.oauth2.handler;

import droni.backend.api.droniuser.exception.DroniUserException;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.config.properties.AppAuthProperties;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static droni.backend.oauth2.DroniCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class DroniOAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private final DroniCookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final AuthTokenProvider tokenProvider;
    private final AppAuthProperties authProperties;
    private final DroniUserRepository userRepository;

    /**
     * 유저가 oauth2 인증에 성공했을 때 -> access token 발급해서 요청했던 페이지에 실어서 redirect
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        OAuth2UserPrincipal oAuth2UserPrincipal = this.getOAuth2UserPrincipal(authentication);
        AuthToken refreshToken = tokenProvider.createRefreshToken();
        setRefreshTokenCookie(response, refreshToken);
        userRepository.updateWithPrincipal(refreshToken.getToken(), oAuth2UserPrincipal);
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        }
        this.clearAuthenticationAttributes(request, response);
        super.getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String defaultTargetUrl = "/auth/callback";
        String redirectUrlString = DroniCookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(defaultTargetUrl);
        OAuth2UserPrincipal oAuth2UserPrincipal = getOAuth2UserPrincipal(authentication);
        AuthToken accessToken = tokenProvider.createAccessAuthToken(oAuth2UserPrincipal.getOAuth2Id());
        return UriComponentsBuilder.fromUriString(redirectUrlString)
                .queryParam("access_token", accessToken.getToken())
                .build().toUriString();
    }
    private void setRefreshTokenCookie(HttpServletResponse response, AuthToken refreshToken) {
        int refreshTokenExpiry = (int) authProperties.getAuth().getRefreshTokenExpiry();
        DroniCookieUtils.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken.getToken(), refreshTokenExpiry);
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        } else {
            throw new DroniUserException(HttpStatus.INTERNAL_SERVER_ERROR, "authentication pricipal not OAuth2UserPriciple");
        }
    }


    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

    }


}
