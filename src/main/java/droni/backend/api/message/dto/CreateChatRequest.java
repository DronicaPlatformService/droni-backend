package droni.backend.api.message.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateChatRequest {
    private Long fromUserId;

    private Long toUserId;

}
