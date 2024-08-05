package droni.backend.api.article.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleSummaryResponse {
    private String articleImageUri;
    private String articleSubject;
}
