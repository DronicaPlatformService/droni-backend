package droni.backend.api.common;

import droni.backend.api.insectcontrol.entity.InsectControlRequest;
import droni.backend.api.insectcontrol.repository.InsectControlRepository;
import droni.backend.global.exception.DroniNotFoundException;
import droni.backend.global.exception.DroniServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DroniServiceStrategy implements InitializingBean {
    private final InsectControlRepository insectControlRepository;
    private Map<DroniServiceKind, DroniServiceSupplier> strategyMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        strategyMap = new HashMap<>();
        strategyMap.put(DroniServiceKind.INSECT_CONTROL, this::fromInsectControl);

        if (strategyMap.size() != DroniServiceKind.values().length) {
            throw new DroniServerException(HttpStatus.INTERNAL_SERVER_ERROR, "drone service kind not supported");
        }

    }

    public DroniServiceDto getDroniServiceInfo(Long id, DroniServiceKind serviceKind) {
        DroniServiceSupplier droniServiceDtoSupplier = strategyMap.get(serviceKind);
        return droniServiceDtoSupplier.getDroniServiceDto(id);
    }

    private DroniServiceDto fromInsectControl(Long id) {
        InsectControlRequest insectControlRequest = insectControlRepository.findById(id)
                .orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "insect control service not found"));
        return DroniServiceDto.builder()
                .serviceId(insectControlRequest.getInsectRequestId())
                .serviceKind(DroniServiceKind.INSECT_CONTROL)
                .build();
    }
}
