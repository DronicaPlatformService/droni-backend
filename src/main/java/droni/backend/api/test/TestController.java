package droni.backend.api.test;

import backend.generated_api.TestApi;
import droni.backend.oauth2.execption.JwtExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController implements TestApi {


    @Override
    public ResponseEntity<String> expireTestGet() {
        throw new JwtExpiredException("JWT is expired");
    }

    @Override
    public ResponseEntity<String> sucessTestGet() {
        return ResponseEntity.ok("Successfully returned");
    }
}
