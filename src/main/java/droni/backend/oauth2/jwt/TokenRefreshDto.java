package droni.backend.oauth2.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshDto {
    private String accessToken;
    private String refreshToken;
}
