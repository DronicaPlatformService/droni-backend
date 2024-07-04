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
    private final int main_view_count = 5;
    private final int all_view_count = 5;

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleDroneContentListGet() {
        List<ArticleSummaryResponse> droneContent = articleService.getDroneContentSummary(all_view_count);
        return ResponseEntity.ok(droneContent);
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleDroneContentSummaryGet() {
        List<ArticleSummaryResponse> droneContent = articleService.getDroneContentSummary(main_view_count);
        return ResponseEntity.ok(droneContent);
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleHowToUseListGet(String articleTarget) {
        List<ArticleSummaryResponse> howToUseArticleSummary = articleService.getHowToUseArticleSummary(articleTarget, all_view_count);
        return ResponseEntity.ok(howToUseArticleSummary);
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleHowToUseSummaryGet(String articleTarget) {
        List<ArticleSummaryResponse> howToUseArticleSummary = articleService.getHowToUseArticleSummary(articleTarget, main_view_count);
        return ResponseEntity.ok(howToUseArticleSummary);
    }


}
