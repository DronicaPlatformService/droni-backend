package droni.backend.oauth2.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

@Data
@Builder
public class TokenRefreshDto {
    private String accessToken;
    @JsonCreator
    public TokenRefreshDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
