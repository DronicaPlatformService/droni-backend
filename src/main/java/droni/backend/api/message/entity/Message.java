package droni.backend.api.message.entity;

import droni.backend.api.attacthfile.entity.DroniFile;
import droni.backend.api.message.dto.ChatMessage;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "message")
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "message_text", nullable = false)
    private String content;

    @Column(name = "message_time", nullable = false)
    @CreatedDate
    private LocalDateTime messageTime;

    @Column(name = "is_expert", nullable = false)
    private boolean isExpert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to")
    private Message reply;

    public ChatMessage toChatMessage() {
        return ChatMessage.builder()
                .messageId(this.messageId)
                .chatroomId(this.chatroom.getChatroomId())
                .repliedMessageId(getRepliedMessageId())
                .content(this.content)
                .timestamp(this.messageTime)
                .build();
    }

    private Long getRepliedMessageId() {
        return Objects.nonNull(this.reply) ? this.reply.getMessageId() : null;
    }
}
