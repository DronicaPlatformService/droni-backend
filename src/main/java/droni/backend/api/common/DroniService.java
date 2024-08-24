package droni.backend.api.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class DroniService {
    @Column(nullable = false, name = "service_request_id")
    private Long serviceId;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DroniServiceKind serviceType;

    public DroniService(@NonNull Long serviceId,@NonNull DroniServiceKind serviceType) {
        this.serviceId = serviceId;
        this.serviceType = serviceType;
    }

    public String getRedirectUrl() {
        return  serviceType.getRedirectUrl() + "/" + serviceId;
    }
}
