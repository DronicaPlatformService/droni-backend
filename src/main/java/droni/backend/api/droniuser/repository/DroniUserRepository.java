package droni.backend.api.droniuser.repository;

import droni.backend.api.droniuser.entity.DroniUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroniUserRepository extends JpaRepository<DroniUser, Integer> , DroniUserQuerydslRepository{
}
