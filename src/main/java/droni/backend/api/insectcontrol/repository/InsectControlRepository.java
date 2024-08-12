package droni.backend.api.insectcontrol.repository;

import droni.backend.api.insectcontrol.entity.InsectControlRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsectControlRepository extends JpaRepository<InsectControlRequest, Long> {
}
