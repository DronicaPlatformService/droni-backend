package droni.backend.oauth2.jwt;

import com.nimbusds.jose.shaded.gson.Gson;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.repository.DroniUserRepository;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TokenRefreshControllerTest {

    @InjectMocks
    private TokenRefreshController tokenRefreshController;
    @Mock
    private AuthTokenProvider tokenProvider;
    @Mock
    private DroniUserRepository userQuerydslRepository;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(tokenRefreshController).build();
    }

    @Test
    @DisplayName("refresh token 정상 발급")
    void reissueSucceclearss() throws Exception {

        //given
        TokenRefreshDto prevToken = TokenRefreshDto.builder().accessToken("expiredToken").refreshToken("refreshToken").build();
        DroniUser mockUser = DroniUser.builder().oauthId("userOauthId").build();
        AuthToken newAccessToken = new AuthToken("newAccessToken", null);
        AuthToken newRefreshToken = new AuthToken("newRefreshToken", null);

        //when
        when(tokenProvider.isExpiredToken(prevToken.getAccessToken())).thenReturn(true);
        when(userQuerydslRepository.findRequestedUser(prevToken)).thenReturn(Optional.of(mockUser));
        when(tokenProvider.createAccessAuthToken(mockUser.getOauthId())).thenReturn(newAccessToken);
        when(tokenProvider.createRefreshToken()).thenReturn(newRefreshToken);
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/reissue").contentType(MediaType.APPLICATION_JSON).content(new Gson().toJson(prevToken))
        );

        //then
        Assertions.assertThat(mockUser.getRefreshToken()).isEqualTo("newRefreshToken");
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccessToken"))
                .andExpect(jsonPath("$.refreshToken").value("newRefreshToken"));
    }
}