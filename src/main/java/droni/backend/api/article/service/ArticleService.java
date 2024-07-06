package droni.backend.api.article.service;

import backend.generated_model.ArticleSummaryResponse;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;
import droni.backend.api.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository repository;


    public List<ArticleSummaryResponse> getHowToUseArticleSummary(String target, int viewCount) {
        ArticleTarget articleTarget = ArticleTarget.fromString(target);
        List<Article> top5Article = repository.findHowToUseArticle(articleTarget, viewCount);
        return top5Article.stream().map(Article::toDto).collect(Collectors.toList());
    }

    public List<ArticleSummaryResponse> getDroneContentSummary(int viewCount) {
        List<Article> mainDroneContent = repository.findDroniContentArticle(viewCount);
        return mainDroneContent.stream().map(Article::toDto).collect(Collectors.toList());
    }

}
