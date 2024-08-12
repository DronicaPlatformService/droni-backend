package droni.backend.api.message.repository;

import droni.backend.api.config.DroniJpaTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
@DroniJpaTest
@Sql(scripts = "/testdata/test-expert.sql")
class ChatroomRepositoryTest {
    @Autowired
    private ChatroomRepository chatroomRepository;



}