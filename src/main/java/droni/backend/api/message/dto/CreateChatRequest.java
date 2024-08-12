package droni.backend.api.message.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateChatRequest {
    @Positive
    private Integer toExpertId;
    @Positive
    private Long serviceId;
    @NotNull
    private DroniServiceKind serviceKind;

}
