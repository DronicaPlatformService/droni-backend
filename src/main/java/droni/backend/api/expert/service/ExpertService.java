package droni.backend.api.expert.service;

import droni.backend.api.expert.dto.ExpertProfile;
import droni.backend.api.expert.repository.DroniExpertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpertService {
    private final DroniExpertRepository droniExpertRepository;
    public List<ExpertProfile> getPopularExpert() {
        return droniExpertRepository.getPopularExpertList();
    }
}
