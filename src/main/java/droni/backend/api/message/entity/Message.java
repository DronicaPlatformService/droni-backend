package droni.backend.api.message.entity;

import droni.backend.api.attacthfile.entity.DroniFile;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.insectcontrol.entity.Land;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    @ManyToOne
    @JoinColumn(name = "message_group_id", nullable = false)
    private MessageGroup group;
    @ManyToOne
    @JoinColumn(name = "message_from", nullable = false)
    private DroniUser fromUser;
    @ManyToOne
    @JoinColumn(name = "message_to", nullable = false)
    private DroniUser toUser;
    @Column(name = "is_pinned") @Builder.Default
    private boolean pinned = false;
    @ManyToOne
    @JoinColumn(name = "reply_message_id", nullable = false)
    private Message reply;
    private String content;
    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private DroniFile file;
    @ManyToOne
    @JoinColumn(name = "land_id", nullable = false)
    private Land land;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
