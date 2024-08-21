package droni.backend.api.expert.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExpertProfile {
    private Integer expertId;
    private Float score;
    private String name;
    private String imageUrl;

    @QueryProjection
    public ExpertProfile(Integer expertId, Float score, String name, String imageUrl) {
        this.expertId = expertId;
        this.score = score;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
