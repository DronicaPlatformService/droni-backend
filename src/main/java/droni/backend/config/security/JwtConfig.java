package droni.backend.config.security;

import droni.backend.config.properties.AppAuthProperties;
import droni.backend.oauth2.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;
    private final AppAuthProperties authProperties;


    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(secret, authProperties);
    }
}
