package droni.backend.api.address.repository;

import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.droniuser.entity.DroniUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUser(DroniUser user);
}
