package droni.backend.api.article.controller;

import droni.backend.api.article.dto.ArticleDetailResponse;
import droni.backend.api.article.dto.ArticleSummaryResponse;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "article", description = "드로니 활용백서, 관련 기사 API")
@RequestMapping(value = "/article", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleController {
    private final ArticleService articleService;
    public static final int HOME_VIEW = 5;
    public static final int ALL_VIEW = -1;

    @GetMapping("/drone-content/list")
    @Operation(summary = "메인화면 드론 콘텐츠 더보기 클릭시 반환 api")
    public List<ArticleSummaryResponse> getDroniContentFullList() {
        return articleService.getDroneContentSummary(ALL_VIEW);
    }

    @GetMapping("/drone-content/summary")
    @Operation(summary = "메인화면 드론 콘텐츠 5개 반환 api")
    public List<ArticleSummaryResponse> getDroniContentSummary() {
        return articleService.getDroneContentSummary(HOME_VIEW);
    }

    @GetMapping("/how-to-use/list")
    @Operation(summary = "드론백서 더보기 클릭시 반환 api")
    public List<ArticleSummaryResponse> getHowToUseFullList(@RequestParam ArticleTarget articleTarget) {
        return articleService.getHowToUseArticleSummary(articleTarget, ALL_VIEW);
    }

    @GetMapping("/how-to-use/summary")
    @Operation(summary = "메인화면에서 보여지는 활용백서 5개 반환 api")
    public List<ArticleSummaryResponse> getHowToUseSummary(@RequestParam ArticleTarget articleTarget) {
        return articleService.getHowToUseArticleSummary(articleTarget, HOME_VIEW);
    }

    @GetMapping("/{articleId}")
    @Operation(summary = "아티클 상세보기")
    public ArticleDetailResponse getArticleSummary(@PathVariable Long articleId) {
        return articleService.getArticleDetail(articleId);
    }

}
