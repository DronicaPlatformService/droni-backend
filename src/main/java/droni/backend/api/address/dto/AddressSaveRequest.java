package droni.backend.api.address.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressSaveRequest {
    private String addressName;
    private boolean isPrimary;
    private String recipientName;
    private String contactNumber;
    private String address1;
    private String address2;

    public void setPrimary(boolean primary) {
        this.isPrimary = primary;
    }
}
