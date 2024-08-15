package droni.backend.api.message.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.api.expert.repository.DroniExpertQuerydslRepository;
import droni.backend.api.message.dto.SocketMessage;
import droni.backend.api.message.entity.Chatroom;
import droni.backend.api.message.entity.Message;
import droni.backend.global.exception.DroniBadRequestException;
import droni.backend.global.exception.DroniNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static droni.backend.api.message.entity.QChatroom.*;

@Repository
@RequiredArgsConstructor
public class MessageQuerydslRepo {
    private final JPAQueryFactory queryFactory;
    private final DroniExpertQuerydslRepository expertRepository;
    private final DroniUserRepository userRepository;
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public void saveMessage(Long roomId, SocketMessage socketMessage) {
        Chatroom chatroomFromMessage = findChatroomFromMessage(roomId, socketMessage);
        this.validateSenderId(socketMessage);
        Message message = socketMessage.toMessageEntity(chatroomFromMessage);
        em.persist(message);
    }

    private Chatroom findChatroomFromMessage(Long roomId, SocketMessage message) {
        Chatroom findChatroom = queryFactory.selectFrom(chatroom)
                .where(chatroom.chatroomId.eq(roomId))
                .fetchOne();
        if (findChatroom == null) {
            throw new DroniNotFoundException(HttpStatus.NOT_FOUND, "Chatroom not found");
        }
        if (findChatroom.isValidMessage(message.getSender(), message.isExpert())) {
            throw new DroniBadRequestException(HttpStatus.BAD_REQUEST, "sendUser is not in chatroom");
        }
        return findChatroom;
    }

    private void validateSenderId(SocketMessage message) {
        if (message.isExpert()) {
            boolean existExpert = expertRepository.isExistExpert(message.getSender().intValue());
            if (!existExpert) {
                throw new DroniNotFoundException(HttpStatus.NOT_FOUND, "Expert not found");
            }
        } else {
            Optional<DroniUser> byId = userRepository.findById(message.getSender());
            if (byId.isEmpty()) {
                throw new DroniNotFoundException(HttpStatus.NOT_FOUND, "User not found");
            }
        }
    }


}
