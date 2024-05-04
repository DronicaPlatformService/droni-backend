package droni.backend.droniuser.entity;

import droni.backend.oauth2.user.ProviderType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class DroniUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String phoneNumber;
    private String email;
    private String name;
    private String profileImage;
    @Column(unique = true, updatable = false)
    private String oauth2id;
    @Enumerated(value = EnumType.STRING)
    private ProviderType provider;
    private String nickname;
    private String timeZone;
    private String address;
    private String addressDetail;
    @Column(unique = true)
    private String refreshToken;
    private boolean notificationEnabled;
    private boolean marketingEnabled;
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;


    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }


}
