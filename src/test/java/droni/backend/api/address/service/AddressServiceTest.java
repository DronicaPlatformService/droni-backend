package droni.backend.api.address.service;

import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.repository.UserAddressRepository;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.global.exception.DroniNotFoundException;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
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
        request.setPrimary(isPrimary);
        return request;
    }

    @BeforeEach
    void setUp() {
        testUser = DroniUser.builder()
                .userId(userId)
                .oauthId(oauthId)
                .build();

        testUserPrincipal = mock(OAuth2UserPrincipal.class);
        when(testUserPrincipal.getOAuth2Id()).thenReturn(oauthId);
    }

    @Test
    @DisplayName("SAVE-SUCCESS-001: 첫 주소 저장 시, 요청과 무관하게 기본 주소로 자동 설정")
    void saveAddress_firstAddressShouldBePrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false);
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(Collections.emptyList());

        // when
        addressService.saveAddress(testUserPrincipal, saveRequest);

        // then
        ArgumentCaptor<UserAddress> addressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(addressCaptor.capture());
        UserAddress savedAddress = addressCaptor.getValue();

        assertThat(savedAddress.isPrimary()).isTrue();
    }

    @Test
    @DisplayName("SAVE-SUCCESS-002: 새로운 기본 주소 저장 시, 기존 기본 주소는 해제")
    void saveAddress_newPrimaryShouldDemoteOldPrimary() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(true);
        UserAddress oldPrimaryAddress = UserAddress.create(
                "oldPrimary", true, "홍길동", "010-1111-2222", "서울시", "강남구", testUser
        );
        UserAddress otherAddress = UserAddress.create(
                "other", false, "김철수", "010-3333-4444", "부산시", "해운대구", testUser
        );

        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(List.of(oldPrimaryAddress, otherAddress));

        // when
        addressService.saveAddress(testUserPrincipal, saveRequest);

        // then
        ArgumentCaptor<UserAddress> addressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(addressCaptor.capture());
        UserAddress newAddress = addressCaptor.getValue();

        assertThat(newAddress.isPrimary()).isTrue();
        assertThat(oldPrimaryAddress.isPrimary()).isFalse();
        assertThat(otherAddress.isPrimary()).isFalse();
    }

    @Test
    @DisplayName("SAVE-SUCCESS-003: 일반 주소 저장 시, 기존 기본 주소에 영향 없음")
    void saveAddress_nonPrimaryShouldRemainNonPrimaryIfPrimaryExists() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false);
        UserAddress existingPrimaryAddress = UserAddress.create(
                "기본주소", true, "홍길동", "010-1234-5678", "서울시", "강남구", testUser
        );

        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(List.of(existingPrimaryAddress));

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
    @DisplayName("SAVE-SUCCESS-004: 기존에 기본 주소 없을 시, 새 주소는 기본 주소로 자동 설정")
    void saveAddress_shouldBecomePrimaryIfNoPrimaryExists() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(false);
        UserAddress nonPrimaryAddress = UserAddress.create(
                "일반주소", false, "김철수", "010-9876-5432", "부산시", "해운대구", testUser
        );

        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(List.of(nonPrimaryAddress));

        // when
        addressService.saveAddress(testUserPrincipal, saveRequest);

        // then
        ArgumentCaptor<UserAddress> addressCaptor = ArgumentCaptor.forClass(UserAddress.class);
        verify(userAddressRepository).save(addressCaptor.capture());
        UserAddress newAddress = addressCaptor.getValue();

        assertThat(newAddress.isPrimary()).isTrue();
        assertThat(nonPrimaryAddress.isPrimary()).isFalse();
    }

    @Test
    @DisplayName("SAVE-FAIL-001: 존재하지 않는 사용자로 주소 저장 시, 예외 발생")
    void saveAddress_shouldThrowException_whenUserNotFound() {
        // given
        AddressSaveRequest saveRequest = createSaveRequest(true);
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(DroniNotFoundException.class, () -> addressService.saveAddress(testUserPrincipal, saveRequest));
        verify(userAddressRepository, never()).save(any(UserAddress.class));
    }

    @Test
    @DisplayName("GET-SUCCESS-001: 주소 목록 조회 성공")
    void getUserAddresses_shouldReturnAddressList() {
        // given
        List<UserAddress> expectedAddresses = List.of(
                UserAddress.create("주소1", true, "홍길동", "010-1234-5678", "서울시", "강남구", testUser),
                UserAddress.create("주소2", false, "김철수", "010-9876-5432", "부산시", "해운대구", testUser)
        );
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(expectedAddresses);

        // when
        List<UserAddress> actualAddresses = addressService.getUserAddresses(testUserPrincipal);

        // then
        assertThat(actualAddresses).hasSize(2);
        assertThat(actualAddresses).isEqualTo(expectedAddresses);
    }

    @Test
    @DisplayName("GET-SUCCESS-002: 주소가 없는 사용자의 목록 조회 시, 빈 리스트 반환")
    void getUserAddresses_shouldReturnEmptyList_whenNoAddresses() {
        // given
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.of(testUser));
        when(userAddressRepository.findByUser(testUser)).thenReturn(Collections.emptyList());

        // when
        List<UserAddress> userAddresses = addressService.getUserAddresses(testUserPrincipal);

        // then
        assertThat(userAddresses).isNotNull();
        assertThat(userAddresses).isEmpty();
    }

    @Test
    @DisplayName("GET-FAIL-001: 존재하지 않는 사용자로 주소 목록 조회 시, 예외 발생")
    void getUserAddresses_shouldThrowException_whenUserNotFound() {
        // given
        when(droniUserRepository.findByOauthId(oauthId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(DroniNotFoundException.class, () -> addressService.getUserAddresses(testUserPrincipal));
        verify(userAddressRepository, never()).findByUser(any(DroniUser.class));
    }
}
