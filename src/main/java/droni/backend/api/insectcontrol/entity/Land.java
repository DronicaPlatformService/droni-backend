package droni.backend.api.insectcontrol.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "land")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Land {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long landId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private DroniUser user;
    private String landAddress;
    private String landAddressDetail;
    private Double landSize;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
