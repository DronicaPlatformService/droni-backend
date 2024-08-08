package droni.backend.api.message.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserChatroom {
    private Long expertId;
    private String expertName;
    private Float expertScore;
    private String expertAddr;
    private String lastMessage;
    private LocalDateTime lastTimestamp;
}
