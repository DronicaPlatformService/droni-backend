package droni.backend.api.message.controller;

import backend.generated_api.ChatApi;
import backend.generated_model.ChatMessage;
import backend.generated_model.CreateChatRequest;
import backend.generated_model.UserChatroom;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/chat")
public class ChatController implements ChatApi {
    @Override
    public ResponseEntity<List<UserChatroom>> chatChatroomUserIdGet(Long userId) {
        return ChatApi.super.chatChatroomUserIdGet(userId);
    }

    @Override
    public ResponseEntity<List<ChatMessage>> chatHistoryChatroomIdGet(Long chatroomId) {
        return ChatApi.super.chatHistoryChatroomIdGet(chatroomId);
    }

    @Override
    public ResponseEntity<Long> chatPost(CreateChatRequest createChatRequest) {
        return ChatApi.super.chatPost(createChatRequest);
    }
}
