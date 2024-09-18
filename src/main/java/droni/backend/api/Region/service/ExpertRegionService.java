package droni.backend.api.Region.service;

import droni.backend.api.Region.repository.ExpertMappingRepository;
import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.api.expert.repository.DroniExpertQuerydslRepository;
import droni.backend.api.Region.entity.ExpertRegionMapping;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExpertRegionService {

    private final ExpertMappingRepository repository;
    private final DroniExpertQuerydslRepository droniExpertQuerydslRepository;
    private final RestTemplate restTemplate;

    public ExpertRegionService(ExpertMappingRepository repository, DroniExpertQuerydslRepository droniExpertQuerydslRepository, RestTemplate restTemplate) {
        this.repository = repository;
        this.droniExpertQuerydslRepository = droniExpertQuerydslRepository;
        this.restTemplate = restTemplate;
    }

    public void saveExpertRegionData(Long expertId) {
        DroniExpert droniExpert = droniExpertQuerydslRepository.findById(expertId.intValue());
        String url = "https://grpc-proxy-server-mkvo6j4wsq-du.a.run.app/v1/regcodes?regcode_pattern=";
        String[] regionData = restTemplate.getForObject(url, String[].class);

        if (regionData != null) {
            for (String region : regionData) {
                String[] splitRegion = region.split(" ");
                String regionCode = splitRegion[0];
                String regionName = splitRegion[1];

                ExpertRegionMapping mapping = new ExpertRegionMapping(droniExpert, regionCode, regionName);
                repository.save(mapping);
            }
        }
    }


}