package droni.backend.oauth2.token;

import droni.backend.oauth2.execption.JWTException;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final Key key;


    public AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createJWT(id, expiry);
    }

    private String createJWT(String id, Date expiry) {
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }
    public String getSubjectFromExpiredJwt() {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(this.token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
        throw new JWTException("Can't get subject from expired jwt");
    }

    public Date getExpiry() {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(this.token).getBody().getExpiration();
    }
}
