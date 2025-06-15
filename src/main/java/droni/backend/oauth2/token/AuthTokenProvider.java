package droni.backend.oauth2.token;


import droni.backend.config.properties.AppAuthProperties;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.execption.JwtExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.security.Principal;
import java.util.*;


@Slf4j
public class AuthTokenProvider {
    private final Key key;
    private final Environment springEnv;
    private final AppAuthProperties authProperties;
    private final Principal NULL_PRINCIPLE = null;
    private final Object NULL_CREDENTIAL = null;

    public AuthTokenProvider(String secret, AppAuthProperties authProperties, Environment env) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.authProperties = authProperties;
        this.springEnv = env;
    }

    public AuthToken createAccessAuthToken(String id) {
        Date now = new Date();
        long accessTokenExpiry = now.getTime() + authProperties.getAuth().getTokenExpiry();
        return new AuthToken(id, new Date(accessTokenExpiry), key);
    }

    public AuthToken createRefreshToken() {
        Date now = new Date();
        long refreshTokenExpiry = now.getTime() + authProperties.getAuth().getRefreshTokenExpiry();
        return new AuthToken(UUID.randomUUID().toString(), new Date(refreshTokenExpiry), key);
    }

    public AuthToken convertToAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(String token) {
        if (isLocalTestRequest(token)) {
            return new TestingAuthenticationToken(NULL_PRINCIPLE, NULL_CREDENTIAL,Collections.emptyList());

        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails user = new User(claims.getSubject(), "", Collections.emptyList());

        return new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());
    }

    public boolean validateToken(String token) {
        if (isLocalTestRequest(token)) {
            return true;
        }
        try {
            // jwt claim parsing 성공시 true 반환
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException exception) {
            throw new JWTException("JWT is not valid");
        } catch (SignatureException exception) {
            throw new JWTException("JWT signature validation fails");
        } catch (IllegalArgumentException exception) {
            throw new JWTException("JWT is null or empty or only whitespace");
        } catch (ExpiredJwtException exception) {
            throw new JwtExpiredException("JWT is expired");
        } catch (Exception exception) {
            log.error("JWT validation fails", exception);
            throw new JWTException("JWT validation fails" + exception.getMessage());
        }
    }

    public boolean isExpiredToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            throw new JWTException("Exception in checking request accessToken's expiry ->" + e.getClass().getSimpleName());
        }
    }
    public String getSubjectFromExpiredJwt(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (Exception e) {
            throw new JWTException("Can't get subject from expired jwt");
        }
    }


    private boolean isLocalTestRequest(String token) {
        List<String> activeProfiles = Arrays.asList(springEnv.getActiveProfiles());
        return activeProfiles.contains("local") && token.equalsIgnoreCase("test");
    }
}
