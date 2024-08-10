package droni.backend.global.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DroniPageRequest {
    @Positive
    @Min(1)
    @NotNull
    private int page;
    @Positive
    @Min(1)
    @NotNull
    private int size;
}
