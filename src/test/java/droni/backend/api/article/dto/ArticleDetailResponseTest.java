package droni.backend.api.article.dto;

import droni.backend.api.article.entity.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleDetailResponseTest {

    @Test
    void whenDisplayImageNull() {
        //given
        Article mockArticle = Mockito.mock(Article.class);
        //then
        Assertions.assertDoesNotThrow(()-> ArticleDetailResponse.fromArticle(mockArticle));
    }
}