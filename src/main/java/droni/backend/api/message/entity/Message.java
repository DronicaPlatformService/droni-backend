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
    @Column(name = "is_pinned")
    @Builder.Default
    private boolean pinned = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_message_id")
    private Message reply;
    @Column(name = "message_content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "file_id")
    private DroniFile file;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    public ChatMessage toChatMessage() {
        return ChatMessage.builder()
                .messageId(this.messageId)
                .chatroomId(this.chatroom.getChatroomId())
                .repliedMessageId(getRepliedMessageId())
                .content(this.content)
                .filePath(getFilePath())
                .timestamp(this.createdAt)
                .build();
    }

    private Long getRepliedMessageId() {
        return Objects.nonNull(this.reply) ? this.reply.getMessageId() : null;
    }

    private String getFilePath() {
        return Objects.nonNull(this.file) ? this.file.getPath() : null;
    }
}
