package droni.backend.api.address.service;

import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.repository.UserAddressRepository;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.global.exception.DroniNotFoundException;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
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

    /**
     * 사용자의 주소를 저장합니다. 첫 주소 또는 기존 주소가 모두 비활성화된 경우 기본 주소로 설정합니다.
     * 기본 주소로 저장 시 기존 기본 주소는 비활성화됩니다.
     */
    public UserAddress saveAddress(OAuth2UserPrincipal userPrincipal, AddressSaveRequest request) {
        DroniUser user = findUserByPrincipal(userPrincipal);

        List<UserAddress> existingAddresses = userAddressRepository.findByUser(user);
        boolean isNewAddressPrimary = request.getIsPrimary();

        if (existingAddresses.isEmpty()) {
            isNewAddressPrimary = true;
        } else if (isNewAddressPrimary) {
            existingAddresses.stream()
                .filter(UserAddress::isPrimary)
                .findFirst()
                .ifPresent(addr -> addr.updatePrimary(false));
        } else if (existingAddresses.stream().noneMatch(UserAddress::isPrimary)) {
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

    /**
     * 사용자의 모든 주소 목록을 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<UserAddress> getUserAddresses(OAuth2UserPrincipal userPrincipal) {
        DroniUser user = findUserByPrincipal(userPrincipal);
        return userAddressRepository.findByUser(user);
    }

    private DroniUser findUserByPrincipal(OAuth2UserPrincipal userPrincipal) {
        return droniUserRepository.findByOauthId(userPrincipal.getOAuth2Id())
            .orElseThrow(() -> new DroniNotFoundException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
    }
}
