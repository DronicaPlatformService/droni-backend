 package droni.backend.api.message.service;

import droni.backend.api.config.DroniJpaTest;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.api.expert.repository.DroniExpertQuerydslRepository;
import droni.backend.api.message.repository.ChatroomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@DroniJpaTest
@ExtendWith(MockitoExtension.class)
@Import({DroniChatService.class, DroniExpertQuerydslRepository.class})
class DroniChatServiceTest {
    @Autowired
    private DroniChatService droniChatService;
    @Autowired
    private ChatroomRepository chatroomRepository;
    @Autowired
    private DroniUserRepository droniUserRepository;
    @Autowired
    private DroniExpertQuerydslRepository expertRepository;

    @Test
    public void test() throws Exception {

        //given

        //when
        //then
    }



}