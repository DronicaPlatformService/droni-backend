package droni.backend.api.article.entity;

import droni.backend.api.attacthfile.entity.DroniFile;
import jakarta.persistence.*;

@Entity
@Table(name = "article_image_mapping")
public class ArticleImageMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private DroniFile file;
}
