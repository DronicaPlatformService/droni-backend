package droni.backend.api.article.entity;

import backend.generated_model.ArticleSummaryResponse;
import droni.backend.api.article.dto.ArticleKind;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.attacthfile.entity.DroniFile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    @Column(nullable = false)
    private String title;
    @Enumerated(value = EnumType.STRING)
    private ArticleKind kind;
    @Column(name = "article_content")
    private String content;
    @Enumerated(value = EnumType.STRING)
    private ArticleTarget target;
    @ManyToOne
    @JoinColumn(name = "display_image", referencedColumnName = "fileId")
    private DroniFile displayImage;
    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ArticleImageMapping> articleImageMappingList = new ArrayList<>();
    private LocalDateTime createdAt;

    @Builder
    public Article(String title, ArticleKind kind, String content, ArticleTarget target, DroniFile displayImage) {
        this.title = title;
        this.kind = kind;
        this.content = content;
        this.target = target;
        this.displayImage = displayImage;
    }

    public ArticleSummaryResponse toDto() {
        ArticleSummaryResponse dto = new ArticleSummaryResponse();
        dto.setArticleImageUri(this.displayImage.getPath());
        dto.setArticleSubject(this.title);
        return dto;
    }
}
