package droni.backend.droniuser.repository;

import droni.backend.droniuser.entity.DroniUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DroniUserRepository extends JpaRepository<DroniUser, Integer> {

    Optional<DroniUser> findDroniUserByOauth2id(String oauth2Id);
}
