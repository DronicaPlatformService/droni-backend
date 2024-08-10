package droni.backend.api.expert.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpertProfile {
    private Integer expertId;
    private Float score;
    private String nickName;
    private String imageUrl;

    @QueryProjection
    public ExpertProfile(Integer expertId, Float score, String nickName, String imageUrl) {
        this.expertId = expertId;
        this.score = score;
        this.nickName = nickName;
        this.imageUrl = imageUrl;
    }
}
