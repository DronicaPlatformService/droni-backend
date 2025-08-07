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
            // 첫 번째 주소는 반드시 기본 주소여야 함
            isNewAddressPrimary = true;
        } else {
            if (isNewAddressPrimary) {
                // 새 주소가 기본 주소로 지정되면 기존 기본 주소를 모두 false로 변경
                existingAddresses.stream()
                    .filter(UserAddress::isPrimary)
                    .forEach(address -> address.setPrimary(false));
            } else {
                // 새 주소가 기본이 아니지만 기존에 기본 주소가 없다면 새 주소를 기본으로 지정
                boolean hasExistingPrimary = existingAddresses.stream()
                    .anyMatch(UserAddress::isPrimary);
                if (!hasExistingPrimary) {
                    isNewAddressPrimary = true;
                }
            }
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
