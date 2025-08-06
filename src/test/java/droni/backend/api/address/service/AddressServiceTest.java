package droni.backend.api.address.service;

import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.repository.UserAddressRepository;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.global.exception.DroniNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private UserAddressRepository userAddressRepository;

    @Mock
    private DroniUserRepository droniUserRepository;

    @InjectMocks
    private AddressService addressService;

    private DroniUser testUser;

    private AddressSaveRequest createSaveRequest(boolean isPrimary) {
        AddressSaveRequest request = new AddressSaveRequest();
        request.setPrimary(isPrimary);
        return request;
    }

    @BeforeEach
    void setUp() {
        testUser = DroniUser.builder().userId(1L).build();
    }

    @Test
    @DisplayName("주소 저장 - 첫 주소는 항상 기본 주소로 설정된다")
    void saveAddress_firstAddressIsPrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false); // Request says not primary
        when(droniUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(List.of()); // No existing
                                                                                // addresses
        when(userAddressRepository.save(any(UserAddress.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserAddress newAddress = addressService.saveAddress(1L, saveRequest);

        // then
        assertThat(newAddress.isPrimary()).isTrue();
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("주소 저장 - 새 주소가 기본 주소로 설정되면 기존 기본 주소는 해제된다")
    void saveAddress_newPrimaryDemotesOldPrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(true);
        UserAddress oldPrimaryAddress =
                UserAddress.builder().user(testUser).isPrimary(true).build();
        UserAddress otherAddress = UserAddress.builder().user(testUser).isPrimary(false).build();

        when(droniUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser))
                .thenReturn(List.of(oldPrimaryAddress, otherAddress));
        when(userAddressRepository.save(any(UserAddress.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserAddress newAddress = addressService.saveAddress(1L, saveRequest);

        // then
        assertThat(newAddress.isPrimary()).isTrue();
        assertThat(oldPrimaryAddress.isPrimary()).isFalse(); // Old primary should be demoted
        assertThat(otherAddress.isPrimary()).isFalse(); // Other address should remain non-primary
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("주소 저장 - 기본 주소가 없는 상태에서 새 주소가 기본이 아니면 새 주소가 기본 주소가 된다")
    void saveAddress_noExistingPrimaryNewNonPrimaryBecomesPrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false); // Request says not primary
        UserAddress nonPrimaryAddress =
                UserAddress.builder().user(testUser).isPrimary(false).build();

        when(droniUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(List.of(nonPrimaryAddress)); // No
                                                                                                 // primary
                                                                                                 // exists
        when(userAddressRepository.save(any(UserAddress.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserAddress newAddress = addressService.saveAddress(1L, saveRequest);

        // then
        assertThat(newAddress.isPrimary()).isTrue(); // Should be promoted to primary
        assertThat(nonPrimaryAddress.isPrimary()).isFalse();
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("주소 저장 - 기본 주소가 있는 상태에서 새 주소가 기본이 아니면 새 주소는 기본이 아니다")
    void saveAddress_existingPrimaryNewNonPrimaryRemainsNonPrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false); // Request says not primary
        UserAddress existingPrimaryAddress =
                UserAddress.builder().user(testUser).isPrimary(true).build();
        UserAddress otherAddress = UserAddress.builder().user(testUser).isPrimary(false).build();

        when(droniUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser))
                .thenReturn(List.of(existingPrimaryAddress, otherAddress));
        when(userAddressRepository.save(any(UserAddress.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserAddress newAddress = addressService.saveAddress(1L, saveRequest);

        // then
        assertThat(newAddress.isPrimary()).isFalse(); // Should remain non-primary
        assertThat(existingPrimaryAddress.isPrimary()).isTrue(); // Existing primary should remain
                                                                 // primary
        assertThat(otherAddress.isPrimary()).isFalse();
        verify(userAddressRepository, times(1)).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("주소 목록 조회")
    void getUserAddresses() {
        // given
        when(droniUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser))
                .thenReturn(List.of(UserAddress.builder().build(), UserAddress.builder().build()));

        // when
        List<UserAddress> userAddresses = addressService.getUserAddresses(1L);

        // then
        assertThat(userAddresses).hasSize(2);
    }

    @Test
    @DisplayName("주소 저장 - 사용자를 찾을 수 없으면 DroniNotFoundException 발생")
    void saveAddress_throwsDroniNotFoundException_whenUserNotFound() {
        // given
        long userId = 1L;
        AddressSaveRequest saveRequest = createSaveRequest(true);
        when(droniUserRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(DroniNotFoundException.class, () -> {
            addressService.saveAddress(userId, saveRequest);
        });

        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("주소 목록 조회 - 사용자를 찾을 수 없으면 DroniNotFoundException 발생")
    void getUserAddresses_throwsDroniNotFoundException_whenUserNotFound() {
        // given
        long userId = 1L;
        when(droniUserRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(DroniNotFoundException.class, () -> {
            addressService.getUserAddresses(userId);
        });

        verify(userAddressRepository, never()).findByUser(any(DroniUser.class));
    }
}
