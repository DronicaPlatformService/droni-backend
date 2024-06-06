package droni.backend.api.article.repository;

import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;

import java.util.List;

public interface ArticleQuerydslRepo {
    List<Article> findMainArticleByTarget(ArticleTarget target);

    List<Article> findMainDroneContent();
}
