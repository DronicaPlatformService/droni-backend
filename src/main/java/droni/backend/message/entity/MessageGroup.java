package droni.backend.message.entity;

import droni.backend.droniuser.entity.DroniUser;
import droni.backend.expert.entity.DroniExpert;
import droni.backend.message.dto.MessageGroupStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message_group")
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageGroupId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DroniUser user;
    @ManyToOne
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert expert;
    @Column(nullable = false)
    private Long serviceRequestId;
    @Enumerated(value = EnumType.ORDINAL)
    private MessageGroupStatus status;
    private LocalDateTime lastConnection;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
