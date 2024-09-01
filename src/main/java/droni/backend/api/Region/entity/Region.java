package droni.backend.api.Region.entity;

import droni.backend.api.expert.entity.DroniExpert;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Table(name = "region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    private String name;
    private String code;

    @ManyToOne
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert expert;
}