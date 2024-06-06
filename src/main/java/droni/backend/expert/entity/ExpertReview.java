package droni.backend.expert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "expert_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long reviewId;
    @ManyToOne
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert expert;
    @Column(nullable = false)
    private Integer score;
    private String content;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
