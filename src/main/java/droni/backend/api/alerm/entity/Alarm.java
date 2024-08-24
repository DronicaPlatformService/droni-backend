package droni.backend.api.alerm.entity;

import droni.backend.api.alerm.enums.AlarmType;
import droni.backend.api.common.DroniService;
import droni.backend.global.exception.DroniServerException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "alarms")
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

    public String getRedirectUrl(Long roomId) {
        if (type != AlarmType.BID_TIME_COMPLETED && Objects.isNull(roomId)) {
            throw new DroniServerException(HttpStatus.INTERNAL_SERVER_ERROR, "room id is required for this alarm type");
        }
        return switch (type) {
            case BID_TIME_COMPLETED -> droniService.getRedirectUrl();
            // websocket subscription url
            case BID_RECEIVED, MESSAGE_RECEIVED -> "/room/" + roomId;
        };
    }
}
