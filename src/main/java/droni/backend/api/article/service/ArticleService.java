package droni.backend.api.article.service;

import backend.generated_model.ArticleSummaryResponse;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;
import droni.backend.api.article.repository.ArticleRepository;
import droni.backend.global.exception.DroniBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository repository;

    public List<ArticleSummaryResponse> getHowToUseArticleSummary(String target) {
        try {
            ArticleTarget articleTarget = ArticleTarget.valueOf(target);
            List<Article> top5Article = repository.findMainArticleByTarget(articleTarget);
            return top5Article.stream().map(Article::toDto).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new DroniBadRequestException(HttpStatus.BAD_REQUEST, String.format("Query parameter target [%s] not supported [ALL, USER, EXPERT] expected", target));
        }
    }

    public List<ArticleSummaryResponse> getDroneContent() {
        List<Article> mainDroneContent = repository.findMainDroneContent();
        return mainDroneContent.stream().map(Article::toDto).collect(Collectors.toList());
    }

}
