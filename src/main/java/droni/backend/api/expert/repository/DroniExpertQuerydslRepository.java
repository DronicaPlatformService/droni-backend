package droni.backend.api.expert.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.expert.dto.ExpertProfile;
import droni.backend.api.expert.dto.QExpertProfile;
import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.global.exception.DroniNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static droni.backend.api.expert.entity.QDroniExpert.droniExpert;
import static droni.backend.api.expert.entity.QExpertReview.expertReview;

@Repository
@RequiredArgsConstructor
public class DroniExpertQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<ExpertProfile> getPopularExpertList() {
        NumberExpression<Float> avgScore = new CaseBuilder()
                .when(expertReview.score.count().gt(0))
                .then(expertReview.score.avg().floatValue())
                .otherwise((Float) null);

        return queryFactory
                .select(new QExpertProfile(droniExpert.expertId, avgScore, droniExpert.user.name, droniExpert.user.profileImage))
                .from(droniExpert)
                .leftJoin(droniExpert.reviews, expertReview)
                .groupBy(droniExpert.expertId)
                .orderBy(avgScore.desc().nullsLast())
                .limit(5)
                .fetch();
    }

    public DroniExpert findById(Integer id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(droniExpert)
                        .where(expertIdEq(id))
                        .fetchOne()
                ).orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "Requested expert not found"));
    }

    private BooleanExpression expertIdEq(Integer id) {
        return droniExpert.expertId.eq(id);
    }
}
