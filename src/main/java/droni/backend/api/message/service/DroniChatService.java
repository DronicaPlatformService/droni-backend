package droni.backend.api.message.service;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.exception.DroniUserException;
import droni.backend.api.droniuser.repository.DroniUserQuerydslRepository;
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
    private final DroniUserQuerydslRepository userQuerydslRepository;
    public List<UserChatroomResponse> getChatroomByUserId(Long userId) {
        List<Chatroom> userChatroomList = chatroomRepository.findByUserId(userId);
        return userChatroomList.stream().map(Chatroom::toUserChatroom).collect(Collectors.toList());
    }

    public Long createChatroom(CreateChatRequest createChatRequest) {
        return null;
    }

    public List<ChatMessage> getMessageByChatroom(Long chatroomId, Long fromMessageId) {
        DroniUser user = userQuerydslRepository.findDroniUserByOauthId().orElseThrow(() -> new DroniUserException(HttpStatus.BAD_REQUEST, "Login user not found"));
        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow(() -> new DroniBadRequestException(HttpStatus.BAD_REQUEST, "Chatroom not found"));
        if (chatroom.getDroniUser() != user) {
            throw new DroniBadRequestException(HttpStatus.BAD_REQUEST, "chatroom does not belong to user");
        }
        List<Message> messages = chatroom.loadMessage(fromMessageId);
        return messages.stream().map(Message::toChatMessage).collect(Collectors.toList());
    }

}
