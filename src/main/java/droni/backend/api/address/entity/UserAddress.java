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
@Table(
    name = "user_address",
    indexes = @Index(name = "idx_user_id", columnList = "user_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    private String addressName;

    @Column(name = "is_primary", nullable = false)
    private boolean primary;

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

    @Builder(access = AccessLevel.PRIVATE)
    public UserAddress(
        String addressName,
        boolean primary,
        String recipientName,
        String contactNumber,
        String address1,
        String address2,
        DroniUser user
    ) {
        this.addressName = addressName;
        this.primary = primary;
        this.recipientName = recipientName;
        this.contactNumber = contactNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.user = user;
    }

    public static UserAddress create(
        String addressName,
        boolean primary,
        String recipientName,
        String contactNumber,
        String address1,
        String address2,
        DroniUser user
    ) {
        return UserAddress.builder()
            .addressName(addressName)
            .primary(primary)
            .recipientName(recipientName)
            .contactNumber(contactNumber)
            .address1(address1)
            .address2(address2)
            .user(user)
            .build();
    }

    public void updateAddress(
        String addressName,
        String recipientName,
        String contactNumber,
        String address1,
        String address2
    ) {
        this.addressName = addressName;
        this.recipientName = recipientName;
        this.contactNumber = contactNumber;
        this.address1 = address1;
        this.address2 = address2;
    }

    public void updatePrimary(boolean primary) {
        this.primary = primary;
    }
}
