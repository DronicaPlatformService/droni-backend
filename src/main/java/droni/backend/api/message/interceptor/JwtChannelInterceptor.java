package droni.backend.api.message.interceptor;

import droni.backend.global.exception.DroniServerException;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final AuthTokenProvider tokenProvider;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            throw new DroniServerException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't find header accessor");
        }
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                tokenProvider.validateToken(token);
            } else {
                throw new JWTException("can't find auth header");
            }
        }
        return message;
    }
}
