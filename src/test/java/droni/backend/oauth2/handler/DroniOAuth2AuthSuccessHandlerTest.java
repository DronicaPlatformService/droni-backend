package droni.backend.oauth2.handler;

import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;

import static droni.backend.oauth2.handler.DroniOAuth2AuthSuccessHandler.REFRESH_QUERY_PARAM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DroniOAuth2AuthSuccessHandlerTest {
    @Mock
    private Environment environment;
    @Mock
    private AuthTokenProvider tokenProvider;
    @InjectMocks
    private DroniOAuth2AuthSuccessHandler successHandler;


    @Test
    void makeRedirectUriWithToken() {
        //given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        OAuth2UserPrincipal mockUserPricipal = mock(OAuth2UserPrincipal.class);

        //when
        when(mockUserPricipal.getOAuth2Id()).thenReturn("testId");
        when(environment.getActiveProfiles()).thenReturn(new String[]{"dev"});
        when(tokenProvider.createAccessAuthToken(mockUserPricipal.getOAuth2Id())).thenReturn(new AuthToken("accessToken", null));
        when(tokenProvider.createRefreshToken()).thenReturn(new AuthToken("refreshToken", null));
        UriComponents uriComponents = successHandler.makeRedirectUriWithToken(mockRequest, mockUserPricipal);

        //then
        MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();
        Assertions.assertTrue(queryParams.containsKey("access_token"));
        Assertions.assertTrue(queryParams.containsKey(REFRESH_QUERY_PARAM));
        assertThat(queryParams.get("access_token").get(0)).isEqualTo("accessToken");
        assertThat(queryParams.get(REFRESH_QUERY_PARAM).get(0)).isEqualTo("refreshToken");
    }
}