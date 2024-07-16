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
    public static final int HOME_VIEW = 5;
    public static final int ALL_VIEW = -1;

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleDroneContentListGet() {
        List<ArticleSummaryResponse> droneContent = articleService.getDroneContentSummary(ALL_VIEW);
        return ResponseEntity.ok(droneContent);
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleDroneContentSummaryGet() {
        List<ArticleSummaryResponse> droneContent = articleService.getDroneContentSummary(HOME_VIEW);
        return ResponseEntity.ok(droneContent);
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleHowToUseListGet(String articleTarget) {
        List<ArticleSummaryResponse> howToUseArticleSummary = articleService.getHowToUseArticleSummary(articleTarget, ALL_VIEW);
        return ResponseEntity.ok(howToUseArticleSummary);
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleHowToUseSummaryGet(String articleTarget) {
        List<ArticleSummaryResponse> howToUseArticleSummary = articleService.getHowToUseArticleSummary(articleTarget, HOME_VIEW);
        return ResponseEntity.ok(howToUseArticleSummary);
    }

    //TODO : implements code from gernerated source




}
