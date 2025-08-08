package droni.backend.api.address.repository;

import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.droniuser.entity.DroniUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUser(DroniUser user);

    // 유저의 기본 주소를 단건 조회
    Optional<UserAddress> findByUserAndPrimaryTrue(DroniUser user);

    // 유저가 주소를 하나라도 갖고 있는지 여부
    boolean existsByUser(DroniUser user);
}
