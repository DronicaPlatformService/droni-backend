package droni.backend.api.article.entity;

import droni.backend.api.article.dto.ArticleKind;
import droni.backend.api.article.dto.ArticleTarget;
import jakarta.persistence.*;

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
    private String content;
    @Enumerated(value = EnumType.STRING)
    private ArticleTarget target;

    @OneToMany(mappedBy = "article", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ArticleImageMapping> articleImageMappingList = new ArrayList<>();

}
