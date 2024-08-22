package droni.backend.api.message.controller;

import droni.backend.api.message.dto.ChatMessage;
import droni.backend.api.message.dto.CreateChatRequest;
import droni.backend.api.message.dto.UserChatroomResponse;
import droni.backend.api.message.service.DroniChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chat")
@RequiredArgsConstructor
@Tag(name = "chat", description = "드로니 채팅 API")
public class ChatController {

    private final DroniChatService droniChatService;

    @GetMapping("/chatroom")
    @Operation(summary = "유저의 채팅방 목록을 가져오는 api")
    public List<UserChatroomResponse> chatChatroomUserIdGet() {
        return droniChatService.getChatroomByUserId();
    }

    @GetMapping("/history/{chatroomId}")
    @Operation(summary = "이전 message 목록을 가져오는 api")
    public List<ChatMessage> chatHistoryChatroomIdGet(@PathVariable("chatroomId") Long chatroomId, @RequestParam(required = false) Long fromMessageId) {
        return droniChatService.getMessageByChatroom(chatroomId, fromMessageId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "채탱방을 생성하는 api")
    public Long chatPost(@Valid CreateChatRequest createChatRequest) {
        return droniChatService.createChatroom(createChatRequest);
    }
}
