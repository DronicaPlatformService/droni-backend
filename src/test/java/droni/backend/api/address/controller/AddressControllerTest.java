package droni.backend.api.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.service.AddressService;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.global.exception.DroniNotFoundException;
import droni.backend.oauth2.jwt.TokenAuthenticationFilter;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.user.impl.NaverOAuth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = AddressController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenAuthenticationFilter.class)
    },
    excludeAutoConfiguration = {
        JpaRepositoriesAutoConfiguration.class,
        DataSourceAutoConfiguration.class
    }
)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AddressService addressService;

    @MockBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private DroniUser testUser;
    private OAuth2UserPrincipal testPrincipal;

    @BeforeEach
    void setUp() {
        long userId = 1L;
        String oauthId = "test-oauth-id";
        testUser = DroniUser.builder()
            .userId(userId)
            .oauthId(oauthId)
            .build();

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("id", oauthId);
        response.put("email", "test@naver.com");
        response.put("name", "테스트유저");
        response.put("nickname", "테스트유저");

        Map<String, Object> attributes = new java.util.HashMap<>();
        attributes.put("response", response);

        NaverOAuth2UserInfo userInfo = new NaverOAuth2UserInfo("dummy-token", attributes);
        testPrincipal = new OAuth2UserPrincipal(userInfo);
    }

    private AddressSaveRequest createAddressSaveRequest() {
        AddressSaveRequest request = new AddressSaveRequest();
        request.setAddressName("테스트 주소");
        request.setRecipientName("홍길동");
        request.setContactNumber("010-1234-5678");
        request.setAddress1("서울시 강남구");
        request.setAddress2("101호");
        request.setIsPrimary(true);
        return request;
    }

    private TestingAuthenticationToken createAuthToken() {
        return new TestingAuthenticationToken(testPrincipal, null, "ROLE_USER");
    }

    @Test
    @DisplayName("POST /api/v1/addresses - 주소지 생성 성공")
    void saveAddress_success() throws Exception {
        // Given
        AddressSaveRequest request = createAddressSaveRequest();
        UserAddress savedAddress = UserAddress.create(
            request.getAddressName(), request.getIsPrimary(), request.getRecipientName(),
            request.getContactNumber(), request.getAddress1(), request.getAddress2(), testUser
        );

        when(addressService.saveAddress(any(OAuth2UserPrincipal.class), any(AddressSaveRequest.class)))
            .thenReturn(savedAddress);

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(post("/api/v1/addresses")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.addressName").value("테스트 주소"))
            .andExpect(jsonPath("$.recipientName").value("홍길동"));

        verify(addressService).saveAddress(any(OAuth2UserPrincipal.class), any(AddressSaveRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/addresses - 인증 정보에 해당하는 사용자를 찾을 수 없을 때 404 반환")
    void saveAddress_userNotFound_shouldReturn404() throws Exception {
        // Given
        AddressSaveRequest request = createAddressSaveRequest();
        when(addressService.saveAddress(any(OAuth2UserPrincipal.class), any(AddressSaveRequest.class)))
            .thenThrow(new DroniNotFoundException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(post("/api/v1/addresses")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(authentication(authentication)).with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("유저를 찾을 수 없습니다."));

        verify(addressService).saveAddress(any(OAuth2UserPrincipal.class), any(AddressSaveRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/addresses - 주소지 목록 조회 성공")
    void getUserAddresses_success() throws Exception {
        // Given
        UserAddress address1 = UserAddress.create("집", true, "홍길동", "010-1111-2222", "서울시 강남구", "101호", testUser);
        UserAddress address2 = UserAddress.create("회사", false, "홍길동", "010-3333-4444", "서울시 서초구", "202호", testUser);

        when(addressService.getUserAddresses(any(OAuth2UserPrincipal.class)))
            .thenReturn(List.of(address1, address2));

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(get("/api/v1/addresses")
                .principal(authentication)
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].addressName").value("집"));

        verify(addressService).getUserAddresses(any(OAuth2UserPrincipal.class));
    }

    @Test
    @DisplayName("GET /api/v1/addresses - 주소지가 없는 경우 빈 리스트 반환")
    void getUserAddresses_emptyList() throws Exception {
        // Given
        when(addressService.getUserAddresses(any(OAuth2UserPrincipal.class)))
            .thenReturn(Collections.emptyList());

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(get("/api/v1/addresses")
                .principal(authentication)
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));

        verify(addressService).getUserAddresses(any(OAuth2UserPrincipal.class));
    }

    @Test
    @DisplayName("GET /api/v1/addresses - 인증 정보에 해당하는 사용자를 찾을 수 없을 때 404 반환")
    void getUserAddresses_userNotFound_shouldReturn404() throws Exception {
        // Given
        when(addressService.getUserAddresses(any(OAuth2UserPrincipal.class)))
            .thenThrow(new DroniNotFoundException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(get("/api/v1/addresses")
                .principal(authentication)
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("유저를 찾을 수 없습니다."));

        verify(addressService).getUserAddresses(any(OAuth2UserPrincipal.class));
    }
}
