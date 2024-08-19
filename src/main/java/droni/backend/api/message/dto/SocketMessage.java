package droni.backend.api.message.dto;

import droni.backend.api.message.entity.Chatroom;
import droni.backend.api.message.entity.Message;
import lombok.Data;

@Data
public class SocketMessage {
    private String messageContent;
    private Long sender;
    private boolean isExpert;

    public Message toMessageEntity(Chatroom room) {
        return Message.builder()
                .chatroom(room)
                .content(messageContent)
                .senderId(sender)
                .isExpert(isExpert)
                .build();
    }
}
