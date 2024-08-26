package droni.backend.api.alerm.service;

import droni.backend.api.alerm.dto.AlarmResponse;
import droni.backend.api.alerm.entity.Alarm;
import droni.backend.api.alerm.repository.AlarmRepository;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.api.message.entity.Chatroom;
import droni.backend.api.message.repository.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final ChatroomRepository chatroomRepository;
    private final AlarmRepository alarmRepository;
    private final DroniUserRepository droniUserRepository;


    public List<AlarmResponse> getUserAlarmList() {
        DroniUser user = droniUserRepository.findUserFromContextHolder();
        List<Alarm> byUserId = alarmRepository.findByUser_UserId(user.getUserId());
        return byUserId.stream().map(alarm -> {
            Long chatroomId = chatroomRepository.findByDroniService(alarm.getDroniService())
                    .map(Chatroom::getChatroomId).orElse(null);
            return AlarmResponse.fromEntity(alarm, chatroomId);
        }).collect(Collectors.toList());
    }
}
