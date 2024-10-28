package droni.backend.api.insectcontrol.entity;

import droni.backend.api.expert.entity.DroniExpert;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "insect_control_received_bid")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InsectControlReceivedBid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long bidId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insect_request_id", nullable = false)
    private InsectControlRequest request;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert expert;
    private Long bidPrice;
    private String bidDescription;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
