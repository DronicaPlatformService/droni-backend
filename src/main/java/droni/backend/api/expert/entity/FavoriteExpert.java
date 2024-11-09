package droni.backend.api.expert.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "favorite_experts")
@EntityListeners(AuditingEntityListener.class)
public class FavoriteExpert {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "expert_id")
    private DroniExpert expert;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private DroniUser user;
    @CreatedDate
    private LocalDateTime createdAt;
}
