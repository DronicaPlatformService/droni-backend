package droni.backend.api.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DroniServiceDto {
    private Long serviceId;
    private DroniServiceKind serviceKind;
}
