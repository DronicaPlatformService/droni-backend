package droni.backend.api.droniuser.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Table(name = "user_social")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSocial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSocialId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private DroniUser user;
    private String socialType;
    private String socialToken;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
