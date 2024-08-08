package droni.backend.api.test;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "test", description = "테스트 컨트롤러")
public class TestController  {

    @GetMapping("/expire-test")
    public ResponseEntity<String> expireTestGet() {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE.value()).build();
    }

    @GetMapping("/success-test")
    public ResponseEntity<String> sucessTestGet() {
        return ResponseEntity.ok("Successfully returned");
    }
}
