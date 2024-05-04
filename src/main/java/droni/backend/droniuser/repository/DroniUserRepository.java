package droni.backend.droniuser.repository;

import droni.backend.droniuser.entity.DroniUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroniUserRepository extends JpaRepository<DroniUser, Integer> {
}
