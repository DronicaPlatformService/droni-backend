package droni.backend.api.address.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    private String addressName;
    private boolean isPrimary;
    private String recipientName;
    private String contactNumber;
    private String address1;
    private String address2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private DroniUser user;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public UserAddress(Long addressId, String addressName, boolean isPrimary, String recipientName,
            String contactNumber, String address1, String address2, DroniUser user) {
        this.addressId = addressId;
        this.addressName = addressName;
        this.isPrimary = isPrimary;
        this.recipientName = recipientName;
        this.contactNumber = contactNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.user = user;
    }

    @Builder
    public UserAddress(String addressName, boolean isPrimary, String recipientName,
            String contactNumber, String address1, String address2, DroniUser user) {
        this.addressName = addressName;
        this.isPrimary = isPrimary;
        this.recipientName = recipientName;
        this.contactNumber = contactNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.user = user;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
