package droni.backend.api.address.service;

import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.repository.UserAddressRepository;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.global.exception.DroniNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {

    private final UserAddressRepository userAddressRepository;
    private final DroniUserRepository droniUserRepository;

    public UserAddress saveAddress(Long userId, AddressSaveRequest request) {
        DroniUser user = droniUserRepository.findById(userId)
            .orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        List<UserAddress> existingAddresses = userAddressRepository.findByUser(user);
        boolean isNewAddressPrimary = request.isPrimary();

        if (existingAddresses.isEmpty()) {
            isNewAddressPrimary = true;
        }
        else if (isNewAddressPrimary) {
            existingAddresses.stream().filter(UserAddress::isPrimary).findFirst()
                    .ifPresent(addr -> addr.setPrimary(false));
        }
        else if (existingAddresses.stream().noneMatch(UserAddress::isPrimary)) {
            isNewAddressPrimary = true;
        }

        UserAddress newAddress = UserAddress.create(
            request.getAddressName(),
            isNewAddressPrimary,
            request.getRecipientName(),
            request.getContactNumber(),
            request.getAddress1(),
            request.getAddress2(),
            user
        );

        return userAddressRepository.save(newAddress);
    }

    @Transactional(readOnly = true)
    public List<UserAddress> getUserAddresses(Long userId) {
        DroniUser user = droniUserRepository.findById(userId)
            .orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
        return userAddressRepository.findByUser(user);
    }
}
