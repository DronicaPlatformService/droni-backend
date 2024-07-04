package droni.backend.api.article.entity;

import backend.generated_model.ArticleSummaryResponse;
import droni.backend.api.article.dto.ArticleKind;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.attacthfile.entity.DroniFile;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    @Column(nullable = false, length = 30)
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


    public ArticleSummaryResponse toDto() {
        ArticleSummaryResponse dto = new ArticleSummaryResponse();
        dto.setArticleImageUri(this.displayImage.getPath());
        dto.setArticleSubject(this.title);
        return dto;
    }
}
