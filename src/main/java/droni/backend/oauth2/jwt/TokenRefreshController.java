package droni.backend.oauth2.jwt;

import backend.generated_model.TokenRefreshDto;
import droni.backend.droniuser.entity.DroniUser;
import droni.backend.droniuser.repository.DroniUserQuerydslRepository;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TokenRefreshController {
    private final AuthTokenProvider tokenProvider;

    private final DroniUserQuerydslRepository userQuerydslRepository;

    @Operation(
        operationId = "reissuePost",
        tags = { "auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "token reissued", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = TokenRefreshDto.class))
            })
        }
    )
    @PostMapping(value = "/reissue")
    @Transactional
    public ResponseEntity<TokenRefreshDto> reissuePost(@Valid @RequestBody TokenRefreshDto prevToken, HttpServletResponse response) throws IOException {
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
                response.sendRedirect(getLoginRedirectUri());
            }
        } else {
            throw new JWTException("not expired token request reissue");
        }
        return null;
    }

    private String getLoginRedirectUri() {
        return UriComponentsBuilder.fromUriString("/login").build().toUriString();
    }





}
