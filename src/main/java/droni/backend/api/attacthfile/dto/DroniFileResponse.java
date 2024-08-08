package droni.backend.api.attacthfile.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DroniFileResponse {
    private Long id;
    private String name;
    private String path;
}
