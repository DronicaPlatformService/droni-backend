package droni.backend.api.Region.entity;

import droni.backend.api.expert.entity.DroniExpert;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "expert_region_mapping")
public class ExpertRegionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert droniExpert;

    private String regionCode;
    private String regionName;

    // 생성자
    public ExpertRegionMapping(DroniExpert droniExpert, String regionCode, String regionName) {
        this.droniExpert = droniExpert;
        this.regionCode = regionCode;
        this.regionName = regionName;
    }

}