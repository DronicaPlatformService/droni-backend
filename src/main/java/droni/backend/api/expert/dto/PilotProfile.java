package droni.backend.api.expert.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PilotProfile {
    private Long expertId;
    private Float score;
    private String nickName;
    private String imageUrl;
}
