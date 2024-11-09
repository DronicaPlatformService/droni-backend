package droni.backend.api.message.service;

import droni.backend.api.common.DroniServiceKind;
import droni.backend.api.common.DroniServiceStrategy;
import droni.backend.api.config.DroniJpaTest;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.api.expert.repository.DroniExpertRepository;
import droni.backend.api.message.dto.CreateChatRequest;
import droni.backend.api.message.entity.Chatroom;
import droni.backend.api.message.repository.ChatroomRepository;
import droni.backend.global.exception.BaseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.mockito.Mockito.*;

@DroniJpaTest
@ExtendWith(MockitoExtension.class)
@Import({DroniChatService.class, DroniExpertRepository.class, DroniServiceStrategy.class})
class DroniChatServiceTest {
    @Autowired
    private DroniChatService droniChatService;
    @SpyBean
    private ChatroomRepository chatroomRepository;
    @SpyBean
    private DroniUserRepository droniUserRepository;
    @MockBean
    private DroniExpertRepository expertRepository;
    @Autowired
    private DroniServiceStrategy strategy;

    private final int expertId = 1;

    @Test
    @DisplayName("채팅방 생성 시 로그인 유저 찾을 수 없을 때 에러")
    public void dataNotFoundTest() throws Exception {
        //given
        CreateChatRequest build = CreateChatRequest.builder().toExpertId(expertId).serviceKind(DroniServiceKind.INSECT_CONTROL).serviceId(1l).build();
        //then
        Assertions.assertThatThrownBy(() -> droniChatService.createChatroom(build)).isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("채팅방 생성 시 요청받은 드론 서비스 요청이 없을 때 에러 발생")
    public void notFoundDroneServiceRequest() throws Exception {
        //given
        CreateChatRequest build = CreateChatRequest.builder().toExpertId(expertId).serviceKind(DroniServiceKind.INSECT_CONTROL).serviceId(1l).build();
        DroniExpert mockExpert = mock(DroniExpert.class);
        DroniUser mockUser = mock(DroniUser.class);

        //when
        doReturn(mockUser).when(droniUserRepository).findUserFromContextHolder();
        when(expertRepository.findById(expertId)).thenReturn(mockExpert);

        //then
        Assertions.assertThatThrownBy(() -> droniChatService.createChatroom(build)).isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("채팅방 메세지 요청시 로그인 유저 소유가 아닌 채팅방 요청시 에러")
    public void notMatchChatroomRequest() throws Exception {
        //given
        long chatroomId = 1L;
        DroniUser mockUser = mock(DroniUser.class);
        Chatroom mockChatroom = mock(Chatroom.class);
        DroniUser mockChatroomOwner = mock(DroniUser.class);
        //when
        doReturn(mockUser).when(droniUserRepository).findUserFromContextHolder();
        doReturn(Optional.of(mockChatroom)).when(chatroomRepository).findById(chatroomId);
        when(mockChatroom.getDroniUser()).thenReturn(mockChatroomOwner);
        when(mockChatroomOwner.getUserId()).thenReturn(11L);
        when(mockUser.getUserId()).thenReturn(12L);
        //then
        Assertions.assertThatThrownBy(() -> droniChatService.getMessageByChatroom(chatroomId, null));
    }
}