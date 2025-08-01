package droni.backend.api.address.dto;

import droni.backend.api.address.entity.UserAddress;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AddressResponse {

    private final Long addressId;
    private final String addressName;
    private final boolean isPrimary;
    private final String recipientName;
    private final String contactNumber;
    private final String address1;
    private final String address2;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AddressResponse(Long addressId, String addressName, boolean isPrimary,
            String recipientName, String contactNumber, String address1, String address2,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.addressId = addressId;
        this.addressName = addressName;
        this.isPrimary = isPrimary;
        this.recipientName = recipientName;
        this.contactNumber = contactNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AddressResponse from(UserAddress address) {
        return AddressResponse.builder().addressId(address.getAddressId())
                .addressName(address.getAddressName()).isPrimary(address.isPrimary())
                .recipientName(address.getRecipientName()).contactNumber(address.getContactNumber())
                .address1(address.getAddress1()).address2(address.getAddress2())
                .createdAt(address.getCreatedAt()).updatedAt(address.getUpdatedAt()).build();
    }
}
