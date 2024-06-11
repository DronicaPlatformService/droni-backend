package droni.backend.api.test;

import backend.generated_api.TestApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController implements TestApi {


    @Override
    public ResponseEntity<String> expireTestGet() {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE.value()).build();
    }

    @Override
    public ResponseEntity<String> sucessTestGet() {
        return ResponseEntity.ok("Successfully returned");
    }
}
