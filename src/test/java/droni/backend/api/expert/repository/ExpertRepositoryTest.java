package droni.backend.api.expert.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.expert.dto.PilotProfile;
import droni.backend.api.config.JpaTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Sql(scripts = "/testdata/test-expert.sql")
@Import(JpaTestConfig.class)
class ExpertRepositoryTest {
    @Autowired
    private ExpertRepository expertRepository;

    @Test
    @DisplayName("조종사를 리뷰 평점 순으로 5명을 가져온다.")
    void 인기조종사조회() {
        final int popularExpertResponseSize = 5;
        List<PilotProfile> popularExpertList = expertRepository.getPopularExpertList();
        assertThat(popularExpertList.size()).isLessThanOrEqualTo(popularExpertResponseSize);
        for (int i = 1; i < popularExpertList.size(); i++) {
            PilotProfile expert = popularExpertList.get(i);
            PilotProfile prevExpert = popularExpertList.get(i - 1);
            assertThat(prevExpert.getScore()).isGreaterThanOrEqualTo(expert.getScore());
        }
    }
    @TestConfiguration
    static class TestQueryDslConfig {
        @Autowired
        JPAQueryFactory jpaQueryFactory;
        @Bean
        public ExpertRepository expertRepository() {
            return new ExpertRepository(jpaQueryFactory);
        }
    }

}