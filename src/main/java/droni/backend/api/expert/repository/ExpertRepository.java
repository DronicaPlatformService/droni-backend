package droni.backend.api.expert.repository;

import backend.generated_model.PilotProfile;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.expert.entity.QDroniExpert;
import droni.backend.api.expert.entity.QExpertReview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ExpertRepository {
    private final JPAQueryFactory queryFactory;
    private final QDroniExpert qDroniExpert = QDroniExpert.droniExpert;
    private final QExpertReview qExpertReview = QExpertReview.expertReview;


    public List<PilotProfile> getPopularExpertList() {
        NumberPath<Double> avgScore = Expressions.numberPath(Double.class, "avg_score");

        List<Tuple> fetch = queryFactory
                .select(qDroniExpert.expertId, qDroniExpert.user.profileImage, qExpertReview.score.avg().as(avgScore))
                .from(qDroniExpert)
                .leftJoin(qDroniExpert.reviews, qExpertReview)
                .groupBy(qDroniExpert.expertId, qDroniExpert.user.profileImage)
                .orderBy(avgScore.desc())
                .limit(5)
                .fetch();
        if (fetch.isEmpty()) {
            return new ArrayList<>();
        }
        return fetch.stream().map(tuple -> new PilotProfile()
                .expertId(Long.valueOf(tuple.get(qDroniExpert.expertId)))
                .imageUrl(tuple.get(qDroniExpert.user.profileImage))
                .score(Objects.isNull(tuple.get(avgScore)) ? 0.0f : tuple.get(avgScore.floatValue()))).collect(Collectors.toList());
    }
}
