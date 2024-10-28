package droni.backend.api.article.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.article.controller.ArticleController;
import droni.backend.api.article.dto.ArticleKind;
import droni.backend.api.article.dto.ArticleTarget;
import droni.backend.api.article.entity.Article;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static droni.backend.api.article.entity.QArticle.article;
import static droni.backend.api.attacthfile.entity.QDroniFile.droniFile;

@Component
@RequiredArgsConstructor
public class ArticleQuerydslRepoImpl implements ArticleQuerydslRepo {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Article> findHowToUseArticle(@NotNull ArticleTarget target, int viewCount) {
        JPAQuery<Article> articleJPAQuery = this.getArticleBasicQuery(this.eqTarget(target));
        if (viewCount == ArticleController.ALL_VIEW) {
            return articleJPAQuery.fetch();
        } else {
            return articleJPAQuery.limit(viewCount).fetch();
        }
    }

    private BooleanExpression eqTarget(ArticleTarget target) {
        return article.target.eq(target).and(article.kind.eq(ArticleKind.HOW_TO_USE));
    }

    @Override
    public List<Article> findDroniContentArticle(int viewCount) {
        JPAQuery<Article> articleBasicQuery = this.getArticleBasicQuery(article.kind.eq(ArticleKind.DRONE_CONTENT));
        if (viewCount == ArticleController.ALL_VIEW) {
            return articleBasicQuery.fetch();
        } else {
            return articleBasicQuery.limit(viewCount).fetch();
        }
    }

    public JPAQuery<Article> getArticleBasicQuery(Predicate articleFilter) {
        return queryFactory.selectFrom(article).leftJoin(article.displayImage, droniFile).fetchJoin()
                .where(articleFilter)
                .orderBy(article.createdAt.desc());
    }


}
