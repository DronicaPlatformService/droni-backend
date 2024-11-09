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
    private int completeRequest;

    @QueryProjection
    public ExpertProfile(Integer expertId, Float score, String name, String imageUrl, int completeRequest) {
        this.expertId = expertId;
        this.score = score;
        this.name = name;
        this.imageUrl = imageUrl;
        this.completeRequest = completeRequest;
    }
}
