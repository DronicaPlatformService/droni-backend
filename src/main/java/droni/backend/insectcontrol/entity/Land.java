package droni.backend.insectcontrol.entity;

import droni.backend.droniuser.entity.DroniUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "land")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Land {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long landId;
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
