package droni.backend.api.message.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.api.message.dto.DroniServiceKind;
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
    private Long serviceId;
    @Enumerated(value = EnumType.ORDINAL)
    private DroniServiceKind service;
    private LocalDateTime lastConnection;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
