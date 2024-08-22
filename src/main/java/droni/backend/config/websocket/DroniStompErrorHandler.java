package droni.backend.config.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import droni.backend.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
@RequiredArgsConstructor
public class DroniStompErrorHandler extends StompSubProtocolErrorHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable deliveryException) {
        try {
            Throwable rootCause = deliveryException.getCause();
            if (BaseException.class.isAssignableFrom(rootCause.getClass())) {
                BaseException exception = (BaseException) rootCause;
                return this.makeErrorMessage(exception);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return super.handleClientMessageProcessingError(clientMessage, deliveryException);
        }
        return super.handleClientMessageProcessingError(clientMessage, deliveryException);
    }

    private Message<byte[]> makeErrorMessage(BaseException exception) throws JsonProcessingException {
        StompErrorResponse stompErrorResponse = new StompErrorResponse(exception);
        StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);
        errorAccessor.setLeaveMutable(true);
        return MessageBuilder.createMessage(objectMapper.writeValueAsBytes(stompErrorResponse), errorAccessor.getMessageHeaders());
    }
}
