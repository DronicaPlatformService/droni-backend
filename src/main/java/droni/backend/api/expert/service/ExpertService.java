package droni.backend.api.expert.service;

import backend.generated_model.PilotProfile;
import droni.backend.api.expert.repository.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;
    public List<PilotProfile> getPopularExpert() {
        return expertRepository.getPopularExpertList();
    }
}
