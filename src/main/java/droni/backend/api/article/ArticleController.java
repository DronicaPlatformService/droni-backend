package droni.backend.api.article;

import backend.generated_api.ArticleApi;
import backend.generated_model.ArticleSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController implements ArticleApi {
    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleDroneContentSummaryGet() {
        return ArticleApi.super.articleDroneContentSummaryGet();
    }

    @Override
    public ResponseEntity<List<ArticleSummaryResponse>> articleHowToUseSummaryGet() {
        return ArticleApi.super.articleHowToUseSummaryGet();
    }
}
