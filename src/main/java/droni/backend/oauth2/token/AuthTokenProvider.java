package droni.backend.oauth2.token;


import droni.backend.config.properties.AppAuthProperties;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.execption.JwtExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;


@Slf4j

public class AuthTokenProvider {
    private final Key key;
    private final AppAuthProperties authProperties;
    public AuthTokenProvider(String secret, AppAuthProperties authProperties) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.authProperties = authProperties;
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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails user = new User(claims.getSubject(), "", Collections.emptyList());

        return new UsernamePasswordAuthenticationToken(user, "", Collections.emptyList());
    }

    public boolean validateToken(String token) {
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
            throw e;
        }
    }
}
