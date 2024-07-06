package droni.backend.api.article.repository;

import droni.backend.api.article.controller.ArticleController;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;
import droni.backend.api.config.JpaTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@Import(JpaTestConfig.class)
@Sql(scripts = "/testdata/test-article.sql")
class ArticleQuerydslRepoTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("드로니 사용자 사용법 기사 조회 repository 테스트")
    void findHowToUseArticle() {
        //when
        List<Article> allHowToUseArticle = articleRepository.findHowToUseArticle(ArticleTarget.USER, ArticleController.ALL_VIEW);
        List<Article> howToUseSummary = articleRepository.findHowToUseArticle(ArticleTarget.USER, ArticleController.HOME_VIEW);

        //then
        Assertions.assertThat(allHowToUseArticle.size()).isEqualTo(7);
        Assertions.assertThat(howToUseSummary.size()).isEqualTo(ArticleController.HOME_VIEW);

    }

    @Test
    @DisplayName("드로니 콘텐츠 조회 respoitory 테스트")
    void findDroniContentArticle() {
        //when
        List<Article> all = articleRepository.findDroniContentArticle(ArticleController.ALL_VIEW);
        List<Article> homeView = articleRepository.findDroniContentArticle(ArticleController.HOME_VIEW);

        //then
        Assertions.assertThat(all.size()).isEqualTo(7);
        Assertions.assertThat(homeView.size()).isEqualTo(ArticleController.HOME_VIEW);

    }
}