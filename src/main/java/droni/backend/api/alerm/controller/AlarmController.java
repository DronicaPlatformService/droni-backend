package droni.backend.api.alerm.controller;

import droni.backend.api.alerm.dto.AlarmResponse;
import droni.backend.api.alerm.service.AlarmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alarm")
@Tag(name = "alarm", description = "알람 API")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    public List<AlarmResponse> getAlarm() {
        return alarmService.getUserAlarmList();
    }

    @GetMapping("/{alarm-id}/redirect-url")
    public String getForwardUrl(@PathVariable("alarm-id") Long alarmId) {
        return alarmService.getForwardUrl(alarmId);
    }
}