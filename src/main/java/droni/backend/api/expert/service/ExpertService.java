package droni.backend.api.expert.service;

import droni.backend.api.expert.dto.DroniPopularExpert;
import droni.backend.api.expert.repository.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;
    public List<DroniPopularExpert> getPopularExpert() {
        return expertRepository.getPopularExpertList();
    }
}
