package droni.backend.api.message.controller;

import droni.backend.api.message.dto.ChatMessage;
import droni.backend.api.message.dto.CreateChatRequest;
import droni.backend.api.message.dto.UserChatroom;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chat")
@Tag(name = "chat", description = "드로니 채팅 API")
public class ChatController  {
    @GetMapping("/chatroom/{userId}")
    @Operation(summary = "유저의 채팅방 목록을 가져오는 api")
    public List<UserChatroom> chatChatroomUserIdGet(@PathVariable("userId")  Long userId) {
        return null;
    }

    @GetMapping("/history/{chatroomId}")
    @Operation(summary = "이전 message 목록을 가져오는 api")
    public List<ChatMessage> chatHistoryChatroomIdGet(@PathVariable("chatroomId") Long chatroomId) {
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "채탱방을 생성하는 api")
    public Long chatPost(CreateChatRequest createChatRequest) {
        return null;
    }
}
