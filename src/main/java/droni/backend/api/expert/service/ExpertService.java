package droni.backend.api.expert.service;

import droni.backend.api.expert.dto.ExpertProfile;
import droni.backend.api.expert.repository.DroniExpertQuerydslRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpertService {
    private final DroniExpertQuerydslRepository droniExpertQuerydslRepository;
    public List<ExpertProfile> getPopularExpert() {
        return droniExpertQuerydslRepository.getPopularExpertList();
    }
}
