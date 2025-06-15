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
}
