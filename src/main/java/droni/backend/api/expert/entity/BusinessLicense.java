package droni.backend.api.expert.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessLicense {
    @Id
    private Integer licenseId;

    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String representative;
    @Column(nullable = false)
    private String companyAddress;
    private String companyAddressDetail;


}
