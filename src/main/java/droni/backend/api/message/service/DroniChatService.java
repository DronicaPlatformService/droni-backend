package droni.backend.api.message.service;

import droni.backend.api.common.DroniService;
import droni.backend.api.common.DroniServiceStrategy;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.api.expert.repository.DroniExpertQuerydslRepository;
import droni.backend.api.message.dto.ChatMessage;
import droni.backend.api.message.dto.CreateChatRequest;
import droni.backend.api.message.dto.UserChatroomResponse;
import droni.backend.api.message.entity.Chatroom;
import droni.backend.api.message.entity.Message;
import droni.backend.api.message.repository.ChatroomRepository;
import droni.backend.global.exception.DroniBadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DroniChatService {
    private final ChatroomRepository chatroomRepository;
    private final DroniUserRepository droniUserRepository;
    private final DroniExpertQuerydslRepository droniExpertQuerydslRepository;
    private final DroniServiceStrategy droniServiceStrategy;

    public List<UserChatroomResponse> getChatroomByUserId() {
        DroniUser loginUser = droniUserRepository.findUserFromContextHolder();
        List<Chatroom> userChatroomList = chatroomRepository.findByUserId(loginUser.getUserId());
        return userChatroomList.stream().map(Chatroom::toUserChatroom).collect(Collectors.toList());
    }


    public Long createChatroom(CreateChatRequest createChatRequest) {
        DroniUser loginUser = droniUserRepository.findUserFromContextHolder();
        DroniExpert chatExpert = droniExpertQuerydslRepository.findById(createChatRequest.getToExpertId());
        DroniService droniService = droniServiceStrategy.getDroniServiceInfo(createChatRequest.getServiceId(), createChatRequest.getServiceKind());
        Chatroom newChatroom = Chatroom.builder()
                .fromUser(loginUser)
                .expert(chatExpert)
                .droniService(droniService)
                .build();
        return chatroomRepository.save(newChatroom).getChatroomId();
    }
    
    public List<ChatMessage> getMessageByChatroom(Long chatroomId, Long fromMessageId) {
        DroniUser loginUser = droniUserRepository.findUserFromContextHolder();
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new DroniBadRequestException(HttpStatus.BAD_REQUEST, "Chatroom not found"));
        if (chatroom.getDroniUser().getUserId() != loginUser.getUserId()) {
            throw new DroniBadRequestException(HttpStatus.BAD_REQUEST, "chatroom does not belong to loginUser");
        }
        List<Message> messages = chatroom.loadMessage(fromMessageId);
        return messages.stream().map(Message::toChatMessage).collect(Collectors.toList());
    }

}
