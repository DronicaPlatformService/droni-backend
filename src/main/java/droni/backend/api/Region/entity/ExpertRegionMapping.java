package droni.backend.api.Region.entity;

import droni.backend.api.expert.entity.DroniExpert;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ExpertRegionMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert droniExpert;


    private String regionCode;
    private String regionName;

    public ExpertRegionMapping() {}

    public ExpertRegionMapping(DroniExpert droniExpert, String regionCode,  String regionName) {
        this.droniExpert = droniExpert;
        this.regionCode = regionCode;
        this.regionName = regionName;
    }






}