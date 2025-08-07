package droni.backend.api.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressSaveRequest {

    @NotBlank(message = "주소 이름은 필수입니다.")
    @Size(max = 50, message = "주소 이름은 50자 이하여야 합니다.")
    private String addressName;

    private boolean isPrimary;

    @NotBlank(message = "수령인 이름은 필수입니다.")
    @Size(max = 20, message = "수령인 이름은 20자 이하여야 합니다.")
    private String recipientName;

    @NotBlank(message = "연락처는 필수입니다.")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "연락처는 숫자만 입력하세요. (국제번호는 +로 시작)")
    @Size(min = 10, max = 15, message = "연락처는 10자 이상 15자 이하여야 합니다.")
    private String contactNumber;

    @NotBlank(message = "주소는 필수입니다.")
    @Size(max = 100, message = "주소는 100자 이하여야 합니다.")
    private String address1;

    @Size(max = 100, message = "상세주소는 100자 이하여야 합니다.")
    private String address2;

    public void setPrimary(boolean primary) {
        this.isPrimary = primary;
    }
}
