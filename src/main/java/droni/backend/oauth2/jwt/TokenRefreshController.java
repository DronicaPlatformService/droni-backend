package droni.backend.oauth2.jwt;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "auth", description = "드로니 인증 API")
public class TokenRefreshController {
    private final AuthTokenProvider tokenProvider;
    private final DroniUserRepository droniUserRepository;

    @Transactional
    @PostMapping(value = "/reissue", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "기존 토근이 만료되었을 때 다시 발생해주는 API")
    public TokenRefreshDto reissuePost(@Valid @RequestBody TokenRefreshDto prevToken) {
        if (tokenProvider.isExpiredToken(prevToken.getAccessToken())) {
            Optional<DroniUser> requestedUser = droniUserRepository.findReissueUser(prevToken.getAccessToken(), prevToken.getRefreshToken());
            if (requestedUser.isPresent()) {
                DroniUser droniUser = requestedUser.get();
                AuthToken accessAuthToken = tokenProvider.createAccessAuthToken(droniUser.getOauthId());
                AuthToken refreshToken = tokenProvider.createRefreshToken();
                droniUser.updateRefreshToken(refreshToken.getToken());
                return TokenRefreshDto.builder()
                        .refreshToken(refreshToken.getToken())
                        .accessToken(accessAuthToken.getToken())
                        .build();
            } else {
                throw new JWTException("No match user with previous oauthId");
            }
        } else {
            throw new JWTException("not expired token request reissue");
        }
    }
}
