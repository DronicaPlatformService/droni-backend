package droni.backend.api.expert.repository;

import droni.backend.api.config.DroniJpaTest;
import droni.backend.api.expert.dto.ExpertProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Sql(scripts = "/testdata/test-expert.sql")
@DroniJpaTest
@Import(DroniExpertRepository.class)
class DroniExpertRepositoryTest {
    @Autowired
    private DroniExpertRepository droniExpertRepository;

    @Test
    @DisplayName("조종사를 리뷰 평점 순으로 5명을 가져온다. 리뷰가 없는 조종사는 null 로 가져온다.")
    void getPopularExpert() {
        List<ExpertProfile> popularExpertList = droniExpertRepository.getPopularExpertList();
        assertThat(popularExpertList).extracting(ExpertProfile::getScore).containsExactly(3.0f, 2.0f, 1.0f, null);
    }

}