package droni.backend.oauth2.handler;

import droni.backend.api.droniuser.exception.DroniUserException;
import droni.backend.api.droniuser.repository.DroniUserQuerydslRepository;
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
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static droni.backend.oauth2.DroniCookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class DroniOAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final Environment environment;
    private final DroniCookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final AuthTokenProvider tokenProvider;
    private final DroniUserQuerydslRepository userRepository;
    //fixme : default target id 변경
    private final String defaultTargetUrl = "/test";

    /**
     * 유저가 oauth2 인증에 성공했을 때 -> access token 발급해서 요청했던 페이지에 실어서 redirect
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserPrincipal oAuth2UserPrincipal = this.getOAuth2UserPrincipal(authentication);
        UriComponents returnUri = this.makeRedirectUriWithToken(request, oAuth2UserPrincipal);
        userRepository.upateWithPricipal(returnUri, oAuth2UserPrincipal);
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + returnUri.toUriString());
        }

        this.clearAuthenticationAttributes(request, response);
        super.getRedirectStrategy().sendRedirect(request, response, returnUri.toUriString());

    }

    /**
     * 로그인 시에 토큰 발급 후에 redirect uri를 반환하는 메소드
     */
    protected UriComponents makeRedirectUriWithToken(HttpServletRequest request, OAuth2UserPrincipal oAuth2UserPrincipal) {
        String redirectUrlString = DroniCookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(defaultTargetUrl);
        if (Objects.isNull(oAuth2UserPrincipal)) {
            return UriComponentsBuilder.fromUriString(redirectUrlString).queryParam("error", "Login failed").build();
        }
        AuthToken accessToken = tokenProvider.createAccessAuthToken(oAuth2UserPrincipal.getOAuth2Id());
        AuthToken refreshToken = tokenProvider.createRefreshToken();
        if (this.isLocalTestRequest()) {
            log.info("accessToken = " + accessToken.getToken());
        }
        return UriComponentsBuilder.fromUriString(redirectUrlString)
                .queryParam("access_token", accessToken.getToken())
                .queryParam("refresh_token", refreshToken.getToken())
                .build();


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
    private boolean isLocalTestRequest() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        return activeProfiles.contains("local");
    }


}
