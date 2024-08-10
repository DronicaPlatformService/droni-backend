package droni.backend.api.expert.service;

import droni.backend.api.expert.repository.ExpertRepository;
import droni.backend.api.expert.dto.ExpertProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpertServiceTest {
    @Mock
    private ExpertRepository expertRepository;

    @InjectMocks
    private ExpertService expertService;

    private final int testSize = 3;


    @Test
    @DisplayName("Repo에서 반환된 popular expert 반환")
    void getPopularExpert() {
        //when
        when(expertRepository.getPopularExpertList()).thenReturn(testRepositoryReturn());
        //then
        List<ExpertProfile> popularExpert = expertService.getPopularExpert();
        assertThat(popularExpert.size()).isEqualTo(testSize);
        for (int i = 0; i < popularExpert.size(); i++) {
            assertThat(popularExpert.get(i).getScore()).isEqualTo(testSize - i);
        }


    }

    private List<ExpertProfile> testRepositoryReturn() {
        List<ExpertProfile> result = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            result.add(ExpertProfile.builder().expertId(i).score((float) (testSize - i)).imageUrl(String.format("%d.jpg", i)).build());
        }
        return result;

    }
}