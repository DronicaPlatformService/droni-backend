package droni.backend.api.expert.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.expert.dto.PilotProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static droni.backend.api.expert.entity.QDroniExpert.droniExpert;
import static droni.backend.api.expert.entity.QExpertReview.expertReview;

@Repository
@RequiredArgsConstructor
public class ExpertRepository {
    private final JPAQueryFactory queryFactory;


    public List<PilotProfile> getPopularExpertList() {
        NumberPath<Double> avgScore = Expressions.numberPath(Double.class, "avg_score");

        List<Tuple> fetch = queryFactory
                .select(droniExpert.expertId, droniExpert.user.profileImage, expertReview.score.avg().as(avgScore))
                .from(droniExpert)
                .leftJoin(droniExpert.reviews, expertReview)
                .groupBy(droniExpert.expertId, droniExpert.user.profileImage)
                .orderBy(avgScore.desc())
                .limit(5)
                .fetch();
        if (fetch.isEmpty()) {
            return new ArrayList<>();
        }
        return fetch.stream().map(tuple ->
                        PilotProfile.builder()
                                .expertId(Long.valueOf(tuple.get(droniExpert.expertId)))
                                .imageUrl(tuple.get(droniExpert.user.profileImage))
                                .score(Objects.isNull(tuple.get(avgScore)) ? 0.0f : tuple.get(avgScore.floatValue()))
                                .build())
                .collect(Collectors.toList());
    }
}
