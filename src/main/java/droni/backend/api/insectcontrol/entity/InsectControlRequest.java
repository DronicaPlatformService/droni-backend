package droni.backend.api.insectcontrol.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.insectcontrol.dto.InsectBidStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "insect_control_request")
public class InsectControlRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insectRequestId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DroniUser user;
    @ManyToOne
    @JoinColumn(name = "land_id", nullable = false)
    private Land land;
    @Enumerated(value = EnumType.ORDINAL)
    private InsectBidStatus status;
    @Column(nullable = false)
    private LocalDateTime requestDate;
    private String requestDescription;
    private String pesticide;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
}
