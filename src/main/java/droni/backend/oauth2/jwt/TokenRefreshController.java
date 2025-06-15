package droni.backend.oauth2.jwt;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.config.properties.AppAuthProperties;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import droni.backend.oauth2.util.DroniCookieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static droni.backend.oauth2.handler.DroniOAuth2AuthSuccessHandler.REFRESH_TOKEN_COOKIE_NAME;

@RestController
@RequiredArgsConstructor
@Tag(name = "auth", description = "드로니 인증 API")
public class TokenRefreshController {
    private final AuthTokenProvider tokenProvider;
    private final AppAuthProperties authProperties;
    private final DroniUserRepository droniUserRepository;

    @Transactional
    @PostMapping(value = "/reissue", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "기존 토근이 만료되었을 때 다시 발생해주는 API")
    public void reissuePost(@Valid @RequestBody TokenRefreshDto prevToken, @RequestParam String redirectionUrl, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = DroniCookieUtils.getCookie(request, REFRESH_TOKEN_COOKIE_NAME)
                .orElseThrow(() -> new JWTException("Can't find refresh token in Cookie")).getValue();
        if (tokenProvider.isExpiredToken(refreshToken)) {
            throw new JWTException("RefreshToken expired, New Login required");
        }
        String oauth2Id = tokenProvider.getSubjectFromExpiredJwt(prevToken.getAccessToken());

        Optional<DroniUser> requestedUser = droniUserRepository.findReissueUser(oauth2Id,  refreshToken);
        if (requestedUser.isPresent()) {
            DroniUser droniUser = requestedUser.get();
            AuthToken newAccessToken = tokenProvider.createAccessAuthToken(droniUser.getOauthId());
            AuthToken newRefreshToken = tokenProvider.createRefreshToken();
            droniUser.updateRefreshToken(newRefreshToken.getToken());
            DroniCookieUtils.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, newRefreshToken.getToken(), authProperties.getRefreshTokenExpiry() );
            String targetUrl = UriComponentsBuilder.fromUriString(redirectionUrl!=null?redirectionUrl:"/auth/callback")
                    .queryParam("access_token", newAccessToken.getToken())
                    .build()
                    .toUriString();
            response.sendRedirect(targetUrl);
        } else {
            throw new JWTException("No match user with previous oauthId");
        }
    }
}
