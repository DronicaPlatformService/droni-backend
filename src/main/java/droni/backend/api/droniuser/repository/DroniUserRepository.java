package droni.backend.api.droniuser.repository;

import droni.backend.api.droniuser.entity.DroniUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroniUserRepository extends JpaRepository<DroniUser, Integer> {

    Optional<DroniUser> findDroniUserByOauthId(String oauthId);
}
