package droni.backend.api.expert.service;

import backend.generated_model.PilotProfile;
import droni.backend.api.expert.repository.ExpertRepository;
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
        List<PilotProfile> popularExpert = expertService.getPopularExpert();
        assertThat(popularExpert.size()).isEqualTo(testSize);
        for (int i = 0; i < popularExpert.size(); i++) {
            assertThat(popularExpert.get(i).getScore()).isEqualTo(testSize - i);
        }


    }

    private List<PilotProfile> testRepositoryReturn() {
        List<PilotProfile> result = new ArrayList<>();
        for (int i = 0; i < testSize; i++) {
            result.add(new PilotProfile().expertId((long) i).score((float) (testSize - i)).imageUrl(String.format("%d.jpg", i)));
        }
        return result;

    }
}