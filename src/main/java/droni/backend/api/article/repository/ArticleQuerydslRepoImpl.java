package droni.backend.api.article.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.article.controller.ArticleController;
import droni.backend.api.article.dto.ArticleKind;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;
import droni.backend.api.article.entity.QArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleQuerydslRepoImpl implements ArticleQuerydslRepo {
    private final JPAQueryFactory queryFactory;
    public static final QArticle qArticle = QArticle.article;

    @Override
    public List<Article> findHowToUseArticle(ArticleTarget target, int viewCount) {
        BooleanBuilder howToUseCondition = new BooleanBuilder();
        howToUseCondition.and(qArticle.target.eq(target));
        howToUseCondition.and(qArticle.kind.eq(ArticleKind.HOW_TO_USE));
        JPAQuery<Article> articleJPAQuery = this.getArticleBasicQuery(howToUseCondition);
        if (viewCount == ArticleController.ALL_VIEW) {
            return articleJPAQuery.fetch();
        } else {
            return articleJPAQuery.limit(viewCount).fetch();
        }
    }

    @Override
    public List<Article> findDroniContentArticle(int viewCount) {
        BooleanBuilder droniContentCondition = new BooleanBuilder();
        droniContentCondition.and(qArticle.kind.eq(ArticleKind.DRONE_CONTENT));
        JPAQuery<Article> articleBasicQuery = this.getArticleBasicQuery(droniContentCondition);
        if (viewCount == ArticleController.ALL_VIEW) {
            return articleBasicQuery.fetch();
        } else {
            return articleBasicQuery.limit(viewCount).fetch();
        }
    }

    public JPAQuery<Article> getArticleBasicQuery(BooleanBuilder booleanBuilder) {
        return queryFactory.selectFrom(qArticle)
                .where(booleanBuilder)
                .orderBy(qArticle.createdAt.desc());
    }


}
