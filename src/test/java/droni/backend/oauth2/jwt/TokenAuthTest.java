package droni.backend.oauth2.jwt;

import droni.backend.config.properties.AppAuthProperties;
import droni.backend.oauth2.execption.JwtExpiredException;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenAuthTest {
    private final String testSecret = "sdfjof195jghg283jghsnvlk186j0135";
    private final Key key = Keys.hmacShaKeyFor(testSecret.getBytes());
    @Mock
    private Environment environment;
    @Mock
    private AppAuthProperties authProperties;

    private AuthTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        // Initialize tokenProvider with mocks
        tokenProvider = new AuthTokenProvider(testSecret, authProperties, environment);
    }

    @Test
    @DisplayName("local 환경에서 validation 통과")
    void localEnvValidation() {
        // given
        when(environment.getActiveProfiles()).thenReturn(new String[]{"local"});
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 600000L);
        AuthToken authToken = new AuthToken("testTokenId", expiry, key);
        //when
        boolean isValidToken = tokenProvider.validateToken(authToken.getToken());
        Assertions.assertTrue(isValidToken);
    }

    @Test
    @DisplayName("정상토큰 validation 통과")
    void notLocalEnvValidation() {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 600000L);
        AuthToken authToken = new AuthToken("testTokenId", expiry, key);
        boolean isValidToken = tokenProvider.validateToken(authToken.getToken());
        Assertions.assertTrue(isValidToken);
    }

    @Test
    @DisplayName("만료 토큰 jwtExpiredException ")
    void 만료된토큰테스트() {
        Date now = new Date();
        long passedDate = now.getTime() - 60000L;
        AuthToken expiredToken = new AuthToken("testTokenId", new Date(passedDate), key);
        assertThatThrownBy(() -> tokenProvider.validateToken(expiredToken.getToken())).isInstanceOf(JwtExpiredException.class);
    }
}