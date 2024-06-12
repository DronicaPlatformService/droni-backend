package droni.backend.api.droniuser.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"test","auth"})
class DroniUserQuerydslRepositoryTest {

    @Test
    void findDroniUserByOauthId() {
    }

    @Test
    void findRequestedUser() {
    }
}