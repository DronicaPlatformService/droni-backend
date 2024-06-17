package droni.backend.api.expert.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.expert.dto.DroniPopularExpert;
import droni.backend.api.expert.entity.QDroniExpert;
import droni.backend.api.expert.entity.QExpertReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ExpertRepository {
    private final JPAQueryFactory queryFactory;
    private final QDroniExpert qDroniExpert = QDroniExpert.droniExpert;
    private final QExpertReview qExpertReview = QExpertReview.expertReview;


    public List<DroniPopularExpert> getPoupularExpert() {
        NumberPath<Double> avgScore = Expressions.numberPath(Double.class, "avg_score");

        List<Tuple> fetch = queryFactory
                .select(qDroniExpert.expertId, qDroniExpert.user.profileImage, qExpertReview.score.avg().as(avgScore))
                .from(qDroniExpert)
                .leftJoin(qDroniExpert.reviews, qExpertReview)
                .groupBy(qDroniExpert.expertId)
                .orderBy(avgScore.desc())
                .limit(5)
                .fetch();


        return fetch.stream().map(tuple -> DroniPopularExpert.builder()
                .expertId(tuple.get(qDroniExpert.expertId))
                .profileImage(tuple.get(qDroniExpert.user.profileImage))
                .score(Objects.isNull(tuple.get(avgScore))? 0.0 : tuple.get(avgScore)).build()).collect(Collectors.toList());
    }
}
