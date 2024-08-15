package droni.backend.api.message.controller;

import droni.backend.api.message.dto.SocketMessage;
import droni.backend.api.message.repository.MessageQuerydslRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageQuerydslRepo messageRepository;

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public SocketMessage sendMessage(@DestinationVariable Long roomId, @Payload SocketMessage message) {
        messageRepository.saveMessage(roomId, message);
        return message;
    }
}
