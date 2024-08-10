package droni.backend.api.message.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.api.message.dto.DroniServiceKind;
import droni.backend.api.message.dto.UserChatroomResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chatroom")
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Chatroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long chatroomId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private DroniUser droniUser;
    @ManyToOne
    @JoinColumn(name = "expert_id", nullable = false)
    private DroniExpert expert;
    @Column(nullable = false, name = "service_request_id")
    private Long serviceId;
    @Enumerated(value = EnumType.STRING)
    private DroniServiceKind serviceType;
    private LocalDateTime lastConnectionTime;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    public UserChatroomResponse toUserChatroom() {
        Message lastMessage = this.getLastMessage();
        return UserChatroomResponse.builder()
                .expert(this.expert)
                .lastMessage(lastMessage)
                .build();
    }

    private Message getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        } else {
            return messages.getFirst();
        }
    }
}
