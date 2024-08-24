package droni.backend.api.alerm.repository;

import droni.backend.api.alerm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByUser_UserId(Long userId);
}
