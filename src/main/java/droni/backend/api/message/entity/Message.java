package droni.backend.api.message.entity;

import droni.backend.api.attacthfile.entity.DroniFile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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
}
