package droni.backend.api.message.dto;

import droni.backend.api.expert.entity.DroniExpert;
import droni.backend.api.message.entity.Message;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserChatroomResponse {
    private Long expertId;
    private String expertName;
    private Double expertScore;
    private String expertAddr;
    private String lastMessage;
    private LocalDateTime lastTimestamp;

    private UserChatroomResponse(Builder builder) {
        this.expertId = builder.expertId;
        this.expertName = builder.expertName;
        this.expertScore = builder.expertScore;
        this.expertAddr = builder.expertAddr;
        this.lastMessage = builder.lastMessage;
        this.lastTimestamp = builder.lastTimestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long expertId;
        private String expertName;
        private Double expertScore;
        private String expertAddr;
        private String lastMessage;
        private LocalDateTime lastTimestamp;

        public Builder expert(DroniExpert expert) {
            this.expertId = Long.valueOf(expert.getExpertId());
            this.expertName = expert.getUser().getName();
            this.expertScore = expert.getExpertScore();
            this.expertAddr = expert.getUser().getAddress();

            return this;
        }

        public Builder lastMessage(Message lastMessage) {
            if (lastMessage == null) {
                return this;
            }
            this.lastMessage = getMessageContentFrom(lastMessage);
            this.lastTimestamp = lastMessage.getMessageTime();
            return this;
        }

        private String getMessageContentFrom(Message message) {
            return message.getContent();
        }

        public UserChatroomResponse build() {
            return new UserChatroomResponse(this);
        }
    }
}
