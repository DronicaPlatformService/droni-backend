package droni.backend.api.alerm.entity;

import droni.backend.api.alerm.enums.AlarmType;
import droni.backend.api.common.DroniService;
import droni.backend.api.common.DroniServiceKind;
import droni.backend.global.exception.DroniServerException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AlarmTest {

    @Test
    @DisplayName("Alarm  객체 생성 테스트 - null check 및 title 만 있을 때")
    public void nullParameterTest() throws Exception{
        //given
        String title = null;
        //when
        //then
        Assertions.assertThatThrownBy(() -> Alarm.builder().type(null).droniService(null).title(title).build())
                .isInstanceOf(NullPointerException.class);
        assertDoesNotThrow(() -> Alarm.builder().type(AlarmType.BID_TIME_COMPLETED).droniService(new DroniService(1L, DroniServiceKind.INSECT_CONTROL)).title(title).build());
    }

    @Test
    @DisplayName("redirection url test")
    public void getRedirectUrlTest() throws Exception {
        //given
        DroniService droniService = new DroniService(1L, DroniServiceKind.INSECT_CONTROL);
        Alarm bidCompletedAlarm = Alarm.builder().type(AlarmType.BID_TIME_COMPLETED).droniService(droniService).title("test").build();
        Alarm bidReceivedAlarm = Alarm.builder().type(AlarmType.BID_RECEIVED).droniService(droniService).title("test").build();
        Alarm messageReceivedAlarm = Alarm.builder().type(AlarmType.MESSAGE_RECEIVED).droniService(droniService).title("test").build();

        //when
        String bidCompletedUrl = bidCompletedAlarm.getRedirectUrl(1L);
        String bidReceivedUrl = bidReceivedAlarm.getRedirectUrl(1L);
        String messageReceivedUrl = messageReceivedAlarm.getRedirectUrl(1L);
        //then
        assertEquals("/room/1", bidReceivedUrl);
        assertEquals("/room/1", messageReceivedUrl);
        assertEquals(droniService.getRedirectUrl(), bidCompletedUrl);
        assertThrows(DroniServerException.class, () -> messageReceivedAlarm.getRedirectUrl(null));
    }

}