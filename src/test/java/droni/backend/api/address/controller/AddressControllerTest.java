package droni.backend.api.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import droni.backend.api.address.dto.AddressSaveRequest;
import droni.backend.api.address.entity.UserAddress;
import droni.backend.api.address.service.AddressService;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.oauth2.jwt.TokenAuthenticationFilter;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.user.impl.NaverOAuth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@WebMvcTest(
    controllers = AddressController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = TokenAuthenticationFilter.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = org.springframework.data.jpa.mapping.JpaMetamodelMappingContext.class)
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
    private DroniUserRepository droniUserRepository;

    @MockBean
    private org.springframework.data.jpa.mapping.JpaMetamodelMappingContext jpaMetamodelMappingContext;

    private DroniUser testUser;
    private OAuth2UserPrincipal testPrincipal;

    @BeforeEach
    void setUp() {
        Long oauthId = 1L;
        testUser = DroniUser.builder()
            .userId(oauthId)
            .oauthId(String.valueOf(oauthId))
            .build();

        // 네이버 OAuth2 응답 구조에 맞게 response 객체 안에 사용자 정보를 넣습니다
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("id", String.valueOf(oauthId));
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
        request.setPrimary(true);
        return request;
    }

    private TestingAuthenticationToken createAuthToken() {
        return new TestingAuthenticationToken(testPrincipal, null, "ROLE_USER");
    }

    private void setAuditFields(UserAddress address, LocalDateTime time) {
        try {
            var createdAtField = UserAddress.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(address, time);
            var updatedAtField = UserAddress.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(address, time);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void contextLoads() {
        // 기본 설정 테스트
    }

    @Test
    @DisplayName("POST /api/v1/addresses - 주소지 생성 성공")
    void saveAddress_success() throws Exception {
        // Given
        AddressSaveRequest request = createAddressSaveRequest();

        UserAddress savedAddress = UserAddress.create(
            request.getAddressName(),
            request.isPrimary(),
            request.getRecipientName(),
            request.getContactNumber(),
            request.getAddress1(),
            request.getAddress2(),
            testUser
        );

        setAuditFields(savedAddress, LocalDateTime.now());

        when(droniUserRepository.findByOauthId(testPrincipal.getOAuth2Id()))
            .thenReturn(Optional.of(testUser));
        when(addressService.saveAddress(eq(testUser.getUserId()), any(AddressSaveRequest.class)))
            .thenReturn(savedAddress);

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(post("/api/v1/addresses")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addressName").value("테스트 주소"))
            .andExpect(jsonPath("$.recipientName").value("홍길동"))
            .andExpect(jsonPath("$.contactNumber").value("010-1234-5678"))
            .andExpect(jsonPath("$.address1").value("서울시 강남구"))
            .andExpect(jsonPath("$.address2").value("101호"))
            .andExpect(jsonPath("$.primary").value(true))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.updatedAt").isNotEmpty());

        verify(droniUserRepository).findByOauthId(testPrincipal.getOAuth2Id());
        verify(addressService).saveAddress(eq(testUser.getUserId()), any(AddressSaveRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/addresses - 인증 정보에 해당하는 사용자를 찾을 수 없을 때 404 반환")
    void saveAddress_userNotFound_shouldReturn404() throws Exception {
        // Given
        AddressSaveRequest request = createAddressSaveRequest();

        when(droniUserRepository.findByOauthId(testPrincipal.getOAuth2Id()))
                .thenReturn(Optional.empty());

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(post("/api/v1/addresses").principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(authentication(authentication)).with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("유저를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.exception").value("DroniNotFoundException"))
            .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
            .andExpect(jsonPath("$.path").value("/api/v1/addresses"))
            .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verify(droniUserRepository).findByOauthId(testPrincipal.getOAuth2Id());
        // AddressService.saveAddress는 호출되지 않아야 함
    }

    @Test
    @DisplayName("GET /api/v1/addresses - 주소지 목록 조회 성공")
    void getUserAddresses_success() throws Exception {
        // Given
        UserAddress address1 = UserAddress.create(
            "집",
            true,
            "홍길동",
            "010-1111-2222",
            "서울시 강남구",
            "101호",
            testUser
        );
        UserAddress address2 = UserAddress.create(
            "회사",
            false,
            "홍길동",
            "010-3333-4444",
            "서울시 서초구",
            "202호",
            testUser
        );

        var now = LocalDateTime.now();
        setAuditFields(address1, now);
        setAuditFields(address2, now);

        when(droniUserRepository.findByOauthId(testPrincipal.getOAuth2Id()))
            .thenReturn(Optional.of(testUser));
        when(addressService.getUserAddresses(testUser.getUserId()))
            .thenReturn(java.util.List.of(address1, address2));

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(get("/api/v1/addresses")
                .principal(authentication)
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].addressName").value("집"))
            .andExpect(jsonPath("$[0].primary").value(true))
            .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
            .andExpect(jsonPath("$[1].addressName").value("회사"))
            .andExpect(jsonPath("$[1].primary").value(false))
            .andExpect(jsonPath("$[1].createdAt").isNotEmpty());

        verify(droniUserRepository).findByOauthId(testPrincipal.getOAuth2Id());
        verify(addressService).getUserAddresses(testUser.getUserId());
    }

    @Test
    @DisplayName("GET /api/v1/addresses - 주소지가 없는 경우 빈 리스트 반환")
    void getUserAddresses_emptyList() throws Exception {
        // Given
        when(droniUserRepository.findByOauthId(testPrincipal.getOAuth2Id()))
            .thenReturn(Optional.of(testUser));
        when(addressService.getUserAddresses(testUser.getUserId()))
            .thenReturn(java.util.Collections.emptyList());

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(get("/api/v1/addresses")
                .principal(authentication)
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));

        verify(droniUserRepository).findByOauthId(testPrincipal.getOAuth2Id());
        verify(addressService).getUserAddresses(testUser.getUserId());
    }

    @Test
    @DisplayName("GET /api/v1/addresses - 인증 정보에 해당하는 사용자를 찾을 수 없을 때 404 반환")
    void getUserAddresses_userNotFound_shouldReturn404() throws Exception {
        // Given
        when(droniUserRepository.findByOauthId(testPrincipal.getOAuth2Id()))
            .thenReturn(Optional.empty());

        TestingAuthenticationToken authentication = createAuthToken();

        // When & Then
        mockMvc.perform(get("/api/v1/addresses")
                .principal(authentication)
                .with(authentication(authentication))
                .with(csrf()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("유저를 찾을 수 없습니다."))
            .andExpect(jsonPath("$.exception").value("DroniNotFoundException"))
            .andExpect(jsonPath("$.httpStatus").value("NOT_FOUND"))
            .andExpect(jsonPath("$.path").value("/api/v1/addresses"))
            .andExpect(jsonPath("$.timestamp").isNotEmpty());

        verify(droniUserRepository).findByOauthId(testPrincipal.getOAuth2Id());
        verify(addressService, never()).getUserAddresses(any(Long.class));
    }
}
