package droni.backend.api.message.controller;

import backend.generated_api.ChatApi;
import backend.generated_model.ChatMessage;
import backend.generated_model.CreateChatRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController implements ChatApi {

    @Override
    public ResponseEntity<List<ChatMessage>> chatHistoryChatroomIdGet(Long chatroomId) {
        return ChatApi.super.chatHistoryChatroomIdGet(chatroomId);
    }

    @Override
    public ResponseEntity<Long> chatPost(CreateChatRequest createChatRequest) {
        return ChatApi.super.chatPost(createChatRequest);
    }
}
