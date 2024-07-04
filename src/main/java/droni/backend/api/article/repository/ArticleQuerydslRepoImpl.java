package droni.backend.api.article.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public List<Article> findHowToUseSummary(ArticleTarget target, int NUMBER_MAIN_VIEW) {
        return queryFactory.selectFrom(qArticle)
                .where(qArticle.target.eq(target)
                        .and(qArticle.kind.eq(ArticleKind.HOW_TO_USE)))
                .orderBy(qArticle.createdAt.desc())
                .limit(NUMBER_MAIN_VIEW)
                .fetch();
    }

    @Override
    public List<Article> findDroneContentSummary(int NUMBER_MAIN_VIEW) {
        return queryFactory.selectFrom(qArticle)
                .where(qArticle.kind.eq(ArticleKind.DRONE_CONTENT))
                .orderBy(qArticle.createdAt.desc())
                .limit(NUMBER_MAIN_VIEW)
                .fetch();
    }

}
