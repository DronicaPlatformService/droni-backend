package droni.backend.api.message.repository;

import droni.backend.api.common.DroniService;
import droni.backend.api.message.entity.Chatroom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    @Query("select ch from Chatroom  ch where ch.droniUser.userId = :userId")
    @EntityGraph(attributePaths = "expert")
    List<Chatroom> findByUserId(@Param("userId") Long userId);

    @Query("select ch from Chatroom ch where ch.droniService = :droniService")
    Chatroom findByDroniService(@Param("droniService") DroniService droniService);
}
