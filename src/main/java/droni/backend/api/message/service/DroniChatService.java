package droni.backend.api.message.service;

import droni.backend.api.common.DroniServiceDto;
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

    public List<UserChatroomResponse> getChatroomByUserId(Long userId) {
        List<Chatroom> userChatroomList = chatroomRepository.findByUserId(userId);
        return userChatroomList.stream().map(Chatroom::toUserChatroom).collect(Collectors.toList());
    }

    public Long createChatroom(CreateChatRequest createChatRequest) {
        DroniUser loginUser = droniUserRepository.findRequestUserFromContext();
        DroniExpert chatExpert = droniExpertQuerydslRepository.findById(createChatRequest.getToExpertId());
        DroniServiceDto droniServiceInfo = droniServiceStrategy.getDroniServiceInfo(createChatRequest.getServiceId(), createChatRequest.getServiceKind());
        Chatroom newChatroom = Chatroom.builder()
                .serviceId(droniServiceInfo.getServiceId())
                .serviceType(droniServiceInfo.getServiceKind())
                .fromUser(loginUser)
                .expert(chatExpert)
                .build();
        return chatroomRepository.save(newChatroom).getChatroomId();
    }
    
    public List<ChatMessage> getMessageByChatroom(Long chatroomId, Long fromMessageId) {
        DroniUser loginUser = droniUserRepository.findRequestUserFromContext();
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new DroniBadRequestException(HttpStatus.BAD_REQUEST, "Chatroom not found"));
        if (chatroom.getDroniUser().getUserId() != loginUser.getUserId()) {
            throw new DroniBadRequestException(HttpStatus.BAD_REQUEST, "chatroom does not belong to loginUser");
        }
        List<Message> messages = chatroom.loadMessage(fromMessageId);
        return messages.stream().map(Message::toChatMessage).collect(Collectors.toList());
    }

}
