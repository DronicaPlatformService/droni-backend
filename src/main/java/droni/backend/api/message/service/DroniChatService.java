package droni.backend.api.message.service;

import droni.backend.api.message.dto.UserChatroomResponse;
import droni.backend.api.message.entity.Chatroom;
import droni.backend.api.message.repository.ChatroomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DroniChatService {
    private final ChatroomRepository chatroomRepository;
    public List<UserChatroomResponse> getChatroomByUserId(Long userId) {
        List<Chatroom> userChatroomList = chatroomRepository.findByUserId(userId);
        return userChatroomList.stream().map(Chatroom::toUserChatroom).collect(Collectors.toList());
    }

}
