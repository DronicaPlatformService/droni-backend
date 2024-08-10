package droni.backend.api.message.repository;

import droni.backend.api.config.DroniJpaTest;
import droni.backend.api.droniuser.entity.DroniUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DroniJpaTest
class ChatroomRepositoryTest {
    @Autowired
    private ChatroomRepository chatroomRepository;


    @BeforeEach
    void init() {
        DroniUser user = mock(DroniUser.class);

    }

    @Test
    void createChatroom() {

    }
}