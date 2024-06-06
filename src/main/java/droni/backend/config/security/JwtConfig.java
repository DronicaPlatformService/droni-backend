package droni.backend.config.security;

import droni.backend.config.properties.AppAuthProperties;
import droni.backend.oauth2.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;
    private final AppAuthProperties authProperties;
    private final Environment env;


    @Bean
    public AuthTokenProvider jwtProvider() {
        return new AuthTokenProvider(secret, authProperties, env);
    }
}
