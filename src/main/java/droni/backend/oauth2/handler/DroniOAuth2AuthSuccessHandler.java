package droni.backend.oauth2.handler;

import droni.backend.oauth2.DroniCookieAuthorizationRequestRepository;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.util.DroniCookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DroniOAuth2AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final DroniCookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final String defaultTargetUrl = "/";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = this.determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to " + targetUrl);
        }

        this.clearAuthenticationAttributes(request);
        super.getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    /**
     * 로그인 시에 토큰 발급 후에 redirect uri를 반환하는 메소드
     */
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUrl = DroniCookieUtils.getCookie(request, "redirect_url")
                .map(Cookie::getValue)
                .orElse(defaultTargetUrl);

        String mode = DroniCookieUtils.getCookie(request, "mode")
                .map(Cookie::getValue)
                .orElse("");
        OAuth2UserPrincipal oAuth2UserPrincipal = this.getOAuth2UserPrincipal(authentication);
        if (Objects.isNull(oAuth2UserPrincipal)) {
            return UriComponentsBuilder.fromUriString(redirectUrl).queryParam("error", "Login failed").build().toUriString();
        }

        if ("login".equalsIgnoreCase(mode)) {
            // todo : db 저장(refresh token)
            // todo : 액세스 토큰, 리프레시 토큰 발급

            String accessToken = null;
            String refreshToken = null;

            return UriComponentsBuilder.fromUriString(redirectUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", refreshToken)
                    .build().toUriString();

        } else if ("unlink".equalsIgnoreCase(mode)) {

            //todo : db 삭제
            //todo : refresh token 삭제
            // unlink logic 추가

            return UriComponentsBuilder.fromUriString(redirectUrl).build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(redirectUrl).queryParam("error", "Login failed").build().toUriString();


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


}
