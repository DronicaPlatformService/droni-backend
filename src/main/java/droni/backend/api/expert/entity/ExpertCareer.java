package droni.backend.api.expert.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "expert_career")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertCareer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long careerId;
    @ManyToOne
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert expert;
    private String title;
    @Column(nullable = false)
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String description;
}
