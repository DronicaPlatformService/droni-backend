package droni.backend.api.expert.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Builder
@Getter
@Table(name = "expert")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DroniExpert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer expertId;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DroniUser user;
    @Builder.Default
    private int completeRequest = 0;
    private String introduction;
    @OneToOne
    @JoinColumn(name = "license_id")
    private BusinessLicense license;
    @OneToMany(mappedBy = "expert")
    private List<ExpertCareer> careers;
    @OneToMany(mappedBy = "expert")
    private List<ExpertReview> reviews;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;


    public Double getExpertScore() {
        OptionalDouble average = this.reviews.stream().mapToInt(ExpertReview::getScore).average();
        return average.isPresent() ? average.getAsDouble() : null;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

}
