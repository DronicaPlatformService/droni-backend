package droni.backend.api.expert.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DroniPopularExpert {
    private int expertId;
    private double score;
    private String profileImage;

}
