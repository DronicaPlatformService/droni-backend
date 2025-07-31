package droni.backend.api.address.controller;

import droni.backend.api.address.dto.AddressResponse;
import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.service.AddressService;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.user.impl.NaverOAuth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @Mock
    private DroniUserRepository droniUserRepository;

    @InjectMocks
    private AddressController addressController;

    private OAuth2UserPrincipal userPrincipal;
    private DroniUser testUser;

    @BeforeEach
    void setUp() {
        Map<String, Object> attributes = Map.of("response",
                Map.of("id", "test-naver-id", "email", "test@naver.com", "name", "test-user",
                        "nickname", "test-nickname", "profile_image", "test-image-url"));
        userPrincipal = new OAuth2UserPrincipal(new NaverOAuth2UserInfo("test-token", attributes));
        testUser = DroniUser.builder().userId(1L).oauthId("test-naver-id").build();
    }

    @Test
    @DisplayName("주소 저장 요청을 보내면, 저장된 주소 정보를 반환한다.")
    void saveAddress() {
        // given
        AddressSaveRequest request = new AddressSaveRequest();
        UserAddress savedAddress =
                new UserAddress(1L, "test-address", false, null, null, null, null, testUser);
        when(droniUserRepository.findByOauthId("test-naver-id")).thenReturn(Optional.of(testUser));
        when(addressService.saveAddress(1L, request)).thenReturn(savedAddress);

        // when
        ResponseEntity<AddressResponse> response =
                addressController.saveAddress(userPrincipal, request);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAddressId()).isEqualTo(1L);
        assertThat(response.getBody().getAddressName()).isEqualTo("test-address");
        verify(addressService, times(1)).saveAddress(1L, request);
    }

    @Test
    @DisplayName("주소 목록 조회 요청을 보내면, 해당 유저의 주소 목록을 반환한다.")
    void getUserAddresses() {
        // given
        when(droniUserRepository.findByOauthId("test-naver-id")).thenReturn(Optional.of(testUser));
        when(addressService.getUserAddresses(1L))
                .thenReturn(List.of(UserAddress.builder().build()));

        // when
        ResponseEntity<List<AddressResponse>> response =
                addressController.getUserAddresses(userPrincipal);

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).hasSize(1);
    }
}
