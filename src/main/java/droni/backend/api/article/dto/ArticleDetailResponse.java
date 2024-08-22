package droni.backend.api.article.dto;

import droni.backend.api.article.entity.Article;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ArticleDetailResponse {
    private String displayImagePath;
    private String title;
    private LocalDateTime createdDate;
    private String content;


    public static ArticleDetailResponse fromArticle(Article article) {
        return ArticleDetailResponse.builder()
                .displayImagePath(article.getDisplayImagePath())
                .title(article.getTitle())
                .content(article.getContent())
                .createdDate(article.getCreatedAt())
                .content(article.getContent())
                .build();
    }

}
