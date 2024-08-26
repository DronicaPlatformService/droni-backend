package droni.backend.api.alerm.entity;

import droni.backend.api.alerm.enums.AlarmType;
import droni.backend.api.common.DroniService;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.global.exception.DroniServerException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "alarms")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "alarm_type", nullable = false)
    private AlarmType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private DroniUser user;
    @CreatedDate
    private LocalDateTime createdAt;
    @Embedded
    private DroniService droniService;
    private String title;


    @Builder
    public Alarm(@NonNull AlarmType type, @NonNull DroniService droniService, String title) {
        this.type = type;
        this.droniService = droniService;
        this.title = title;
    }

    public String getRedirectUrl(Long chatroomId) {
        if (type != AlarmType.BID_TIME_COMPLETED && Objects.isNull(chatroomId)) {
            throw new DroniServerException(HttpStatus.INTERNAL_SERVER_ERROR, "room id is required for this alarm type");
        }
        return switch (type) {
            case BID_TIME_COMPLETED -> droniService.getRedirectUrl();
            // websocket subscription url
            case BID_RECEIVED, MESSAGE_RECEIVED -> "/room/" + chatroomId;
        };
    }
}
