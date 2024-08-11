package droni.backend.api.message.entity;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.api.message.dto.DroniServiceKind;
import droni.backend.api.message.dto.UserChatroomResponse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "chatroom")
@Getter
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
    @BatchSize(size = 100)
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

    public List<Message> loadMessage(Long loadedMessageId) {
        int returnCount = Objects.isNull(loadedMessageId) ? 20 : 10;
        return this.messages.stream()
                .filter(message -> Objects.isNull(loadedMessageId) || message.getMessageId() < loadedMessageId)
                .limit(returnCount)
                .collect(Collectors.toList());

    }
}
