package droni.backend.api.Region.repository;

import droni.backend.api.Region.entity.ExpertRegionMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpertMappingRepository extends JpaRepository<ExpertRegionMapping, Long> {
    List<ExpertRegionMapping> findByRegionCode(String regionCode);
}
