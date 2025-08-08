package droni.backend.api.address.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.repository.UserAddressRepository;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.global.exception.DroniNotFoundException;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private UserAddressRepository userAddressRepository;

    @Mock
    private DroniUserRepository droniUserRepository;

    @InjectMocks
    private AddressService addressService;

    private DroniUser testUser;
    private OAuth2UserPrincipal testUserPrincipal;
    private final Long userId = 1L;
    private final String oauthId = "test-oauth-id";

    private AddressSaveRequest createSaveRequest(boolean isPrimary) {
        AddressSaveRequest request = new AddressSaveRequest();
        request.setAddressName("테스트 주소");
        request.setRecipientName("테스트 수령인");
        request.setContactNumber("010-0000-0000");
        request.setAddress1("테스트 주소1");
        request.setAddress2("테스트 주소2");
        request.setIsPrimary(isPrimary);
        return request;
    }

    @BeforeEach
    void setUp() {
        testUser = DroniUser.builder().userId(userId).oauthId(oauthId).build();

        testUserPrincipal = mock(OAuth2UserPrincipal.class);
        when(testUserPrincipal.getOAuth2Id()).thenReturn(oauthId);
    }

    @Test
    @DisplayName("첫 주소 저장 시 기본 주소로 자동 설정된다")
    void saveAddress_firstAddressShouldBePrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false);
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.existsByUser(testUser)).thenReturn(false);
        when(userAddressRepository.findByUserAndPrimaryTrue(testUser)).thenReturn(Optional.empty());

        // when
        addressService.saveAddress(testUserPrincipal, saveRequest);

        // then
        ArgumentCaptor<UserAddress> addressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(addressCaptor.capture());
        UserAddress savedAddress = addressCaptor.getValue();

        assertThat(savedAddress.isPrimary()).isTrue();
    }

    @Test
    @DisplayName("새로운 기본 주소 저장 시 기존 기본 주소가 해제된다")
    void saveAddress_newPrimaryShouldDemoteOldPrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(true);
        UserAddress oldPrimaryAddress =
                UserAddress.create(
                        "oldPrimary",
                        true,
                        "홍길동",
                        "010-1111-2222",
                        "서울시",
                        "강남구",
                        testUser);

        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.existsByUser(testUser)).thenReturn(true);
        when(userAddressRepository.findByUserAndPrimaryTrue(testUser))
                .thenReturn(Optional.of(oldPrimaryAddress));

        // when
        addressService.saveAddress(testUserPrincipal, saveRequest);

        // then
        ArgumentCaptor<UserAddress> addressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(addressCaptor.capture());
        UserAddress newAddress = addressCaptor.getValue();

        assertThat(newAddress.isPrimary()).isTrue();
        assertThat(oldPrimaryAddress.isPrimary()).isFalse();
    }

    @Test
    @DisplayName("일반 주소 저장 시 기존 기본 주소에 영향을 주지 않는다")
    void saveAddress_nonPrimaryShouldRemainNonPrimaryIfPrimaryExists() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false);
        UserAddress existingPrimaryAddress =
                UserAddress.create(
                        "기본주소",
                        true,
                        "홍길동",
                        "010-1234-5678",
                        "서울시",
                        "강남구",
                        testUser);

        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.existsByUser(testUser)).thenReturn(true);
        when(userAddressRepository.findByUserAndPrimaryTrue(testUser))
                .thenReturn(Optional.of(existingPrimaryAddress));

        // when
        addressService.saveAddress(testUserPrincipal, saveRequest);

        // then
        ArgumentCaptor<UserAddress> addressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(addressCaptor.capture());
        UserAddress newAddress = addressCaptor.getValue();

        assertThat(newAddress.isPrimary()).isFalse();
        assertThat(existingPrimaryAddress.isPrimary()).isTrue();
    }

    @Test
    @DisplayName("기존에 기본 주소가 없으면 새 주소가 기본 주소로 자동 설정된다")
    void saveAddress_shouldBecomePrimaryIfNoPrimaryExists() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false);
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.existsByUser(testUser)).thenReturn(true);
        when(userAddressRepository.findByUserAndPrimaryTrue(testUser))
                .thenReturn(Optional.empty());

        // when
        addressService.saveAddress(testUserPrincipal, saveRequest);

        // then
        ArgumentCaptor<UserAddress> addressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(addressCaptor.capture());
        UserAddress newAddress = addressCaptor.getValue();

        assertThat(newAddress.isPrimary()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 주소 저장 시 예외가 발생한다")
    void saveAddress_shouldThrowException_whenUserNotFound() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(true);
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(
                DroniNotFoundException.class, () -> addressService.saveAddress(testUserPrincipal, saveRequest));
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 주소 목록 조회 시 예외가 발생한다")
    void getUserAddresses_shouldThrowException_whenUserNotFound() {
        // given
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(DroniNotFoundException.class, () -> addressService.getUserAddresses(testUserPrincipal));
        verify(userAddressRepository, never()).findByUser(any(DroniUser.class));
    }
}
