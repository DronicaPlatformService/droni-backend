package droni.backend.api.Region.service;

import droni.backend.api.Region.repository.ExpertMappingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpertRegionService {

    private final ExpertMappingRepository repository;



}
