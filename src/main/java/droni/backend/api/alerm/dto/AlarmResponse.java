package droni.backend.api.alerm.dto;

import droni.backend.api.alerm.entity.Alarm;
import droni.backend.api.alerm.enums.AlarmType;
import droni.backend.api.common.DroniServiceKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponse {
    private AlarmType type;
    private String message;
    private int afterCreated;
    private DroniServiceKind serviceKind;

    public static AlarmResponse fromEntity(Alarm alarm) {
        return AlarmResponse.builder()
                .type(alarm.getType())
                .message(alarm.getTitle())
                .afterCreated((int) ChronoUnit.DAYS.between(alarm.getCreatedAt(), LocalDateTime.now(Clock.systemUTC())))
                .serviceKind(alarm.getDroniService().getServiceType())
                .build();
    }
}
