package droni.backend.api.article.controller;

import backend.generated_api.ArticleApi;
import backend.generated_model.ArticleSummaryResponse;
import droni.backend.api.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController implements ArticleApi {
    private final ArticleService articleService;
    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleDroneContentSummaryGet() {
        List<ArticleSummaryResponse> droneContent = articleService.getDroneContent();
        return ResponseEntity.ok(droneContent);
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleHowToUseSummaryGet(String articleTarget) {
        List<ArticleSummaryResponse> howToUseArticleSummary = articleService.getHowToUseArticleSummary(articleTarget);
        return ResponseEntity.ok(howToUseArticleSummary);
    }
}
