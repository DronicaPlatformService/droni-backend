package droni.backend.oauth2.jwt;

import backend.generated_api.AuthApi;
import backend.generated_model.TokenRefreshDto;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserQuerydslRepository;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TokenRefreshController implements AuthApi {
    private final AuthTokenProvider tokenProvider;

    private final DroniUserQuerydslRepository userQuerydslRepository;

    @Override
    @Transactional
    public ResponseEntity<TokenRefreshDto> reissuePost(@Valid @RequestBody TokenRefreshDto prevToken) {
        if (tokenProvider.isExpiredToken(prevToken.getAccessToken())) {
            Optional<DroniUser> requestedUser = userQuerydslRepository.findRequestedUser(prevToken);
            if (requestedUser.isPresent()) {
                DroniUser droniUser = requestedUser.get();
                AuthToken accessAuthToken = tokenProvider.createAccessAuthToken(droniUser.getOauthId());
                AuthToken refreshToken = tokenProvider.createRefreshToken();
                droniUser.updateRefreshToken(refreshToken.getToken());
                TokenRefreshDto tokenRefreshDto = new TokenRefreshDto();
                tokenRefreshDto.setRefreshToken(refreshToken.getToken());
                tokenRefreshDto.setAccessToken(accessAuthToken.getToken());
                return ResponseEntity.ok(tokenRefreshDto);
            } else {
                throw new JWTException("No match user with previous oauthId");
            }
        } else {
            throw new JWTException("not expired token request reissue");
        }
    }




}
