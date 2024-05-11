package droni.backend.test;

import backend.generated_api.TestApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController implements TestApi {
    @Override
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok("Ok");
    }

    @Override
    public ResponseEntity<String> test2Get() {
        return ResponseEntity.ok("Test2");
    }
}
