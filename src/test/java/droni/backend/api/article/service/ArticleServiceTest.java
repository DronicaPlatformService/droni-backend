package droni.backend.api.article.service;

import droni.backend.api.article.dto.ArticleSummaryResponse;
import droni.backend.api.article.controller.ArticleController;
import droni.backend.api.article.dto.ArticleKind;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;
import droni.backend.api.article.repository.ArticleRepository;
import droni.backend.api.attacthfile.entity.DroniFile;
import droni.backend.global.exception.DroniBadRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    @DisplayName("콘텐츠 타겟 잘못들어왔을 때 400")
    void checkArticleTargetRequest() {
        Assertions.assertThat(ArticleTarget.fromString("ALL")).isEqualTo(ArticleTarget.ALL);
        Assertions.assertThat(ArticleTarget.fromString("User")).isEqualTo(ArticleTarget.USER);
        Assertions.assertThat(ArticleTarget.fromString("USER")).isEqualTo(ArticleTarget.USER);
        Assertions.assertThat(ArticleTarget.fromString("expert")).isEqualTo(ArticleTarget.EXPERT);

        Assertions.assertThatThrownBy(() -> ArticleTarget.fromString("WRONG")).isInstanceOf(DroniBadRequestException.class);

    }


    @Test
    @DisplayName("홈화면에서 보이는 사용법 콘텐츠 조회로직 검사")
    void getHowToUseArticleSummary() {
        //given
        List<Article> howToUseSummary = this.testHowToUseSummary(ArticleKind.HOW_TO_USE);
        //when
        when(articleRepository.findHowToUseArticle(ArticleTarget.USER, ArticleController.HOME_VIEW)).thenReturn(howToUseSummary);
        List<ArticleSummaryResponse> expectedResults = howToUseSummary.stream().map(Article::toDto).collect(Collectors.toList());
        List<ArticleSummaryResponse> returnedResults = articleService.getHowToUseArticleSummary(ArticleTarget.USER, ArticleController.HOME_VIEW);
        //then
        Assertions.assertThat(expectedResults.containsAll(returnedResults)).isTrue();
    }


    @Test
    @DisplayName("홈화면에서 보이는 드론 콘텐츠 조회로직 검사")
    void getDroneContentSummary() {
        //given
        List<Article> articles = this.testHowToUseSummary(ArticleKind.DRONE_CONTENT);
        //when
        when(articleRepository.findDroniContentArticle(ArticleController.HOME_VIEW)).thenReturn(articles);
        List<ArticleSummaryResponse> expectedResults = articles.stream().map(Article::toDto).collect(Collectors.toList());
        List<ArticleSummaryResponse> droneContentSummary = articleService.getDroneContentSummary(ArticleController.HOME_VIEW);
        //then
        Assertions.assertThat(expectedResults.containsAll(droneContentSummary)).isTrue();
    }


    private List<Article> testHowToUseSummary(ArticleKind kind) {
        List<Article> howToUseArticles = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            howToUseArticles.add(Article.builder()
                    .title("title" + i)
                    .displayImage(DroniFile.builder().path("imagePath" + i).build())
                    .target(ArticleTarget.USER)
                    .kind(kind)
                    .build());
        }
        return howToUseArticles;
    }
}