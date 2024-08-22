package droni.backend.api.article.service;

import droni.backend.api.article.dto.ArticleDetailResponse;
import droni.backend.api.article.dto.ArticleSummaryResponse;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;
import droni.backend.api.article.repository.ArticleRepository;
import droni.backend.global.exception.DroniNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {
    private final ArticleRepository repository;


    public List<ArticleSummaryResponse> getHowToUseArticleSummary(ArticleTarget target, int viewCount) {
        List<Article> top5Article = repository.findHowToUseArticle(target, viewCount);
        return top5Article.stream().map(Article::toDto).collect(Collectors.toList());
    }

    public List<ArticleSummaryResponse> getDroneContentSummary(int viewCount) {
        List<Article> mainDroneContent = repository.findDroniContentArticle(viewCount);
        return mainDroneContent.stream().map(Article::toDto).collect(Collectors.toList());
    }

    public ArticleDetailResponse getArticleDetail(Long articleId) {
        Article findArticle = repository.findById(articleId)
                .orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "Can't find article"));
        return ArticleDetailResponse.fromArticle(findArticle);
    }

}
