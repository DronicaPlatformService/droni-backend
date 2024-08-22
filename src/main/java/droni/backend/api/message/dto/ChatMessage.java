package droni.backend.api.message.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessage {
    private long messageId;
    private long chatroomId;
    private Long repliedMessageId;
    private String content;
    private LocalDateTime timestamp;
}
