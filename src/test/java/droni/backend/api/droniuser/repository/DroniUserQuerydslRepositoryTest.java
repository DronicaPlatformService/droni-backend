package droni.backend.api.droniuser.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DataJpaTest
@Sql(scripts = "/testdata/test-user-set.sql")
@ExtendWith(MockitoExtension.class)
class DroniUserQuerydslRepositoryTest {
    @Autowired
    private DroniUserQuerydslRepository userQuerydslRepository;

    @Test
    @DisplayName("유저 조회 정상 작동 테스트")
    void findDroniUserByOauthId() {
        //given
        DroniUser droniUser = userQuerydslRepository.findDroniUserByOauthId("3497839913").orElse(null);
        //then
        org.junit.jupiter.api.Assertions.assertTrue(Objects.nonNull(droniUser));
        assertThat(droniUser.getUserId()).isEqualTo(4);
    }

    @Test
    @DisplayName("Principal 유저 업데이트 테스트")
    void 사용자인증으로유저업데이트생성테스트() {
        final String TEST_TOKEN = "testRefreshToken";
        //given
        OAuth2UserPrincipal mockPrincipal = mock(OAuth2UserPrincipal.class);
        //when
        when(mockPrincipal.getOAuth2Id()).thenReturn("3497839913");
        DroniUser updatedUser = userQuerydslRepository.updateWithPrincipal(TEST_TOKEN, mockPrincipal);
        //then
        assertThat(updatedUser.getRefreshToken()).isEqualTo(TEST_TOKEN);
    }

    @Test
    @DisplayName("로그인 시 새로운 유저 생성 테스트")
    void 첫로그인시에사용자생성테스트() {
        //given
        final String NEW_USER_REFRESH_TOKEN = "newTestRefreshToken";
        final String NEW_USER_OAUTH_ID = "newTestUser";
        OAuth2UserPrincipal mockPrincipal = mock(OAuth2UserPrincipal.class);
        //when
        when(mockPrincipal.getOAuth2Id()).thenReturn("NEW_USER_OAUTH_ID");
        when(mockPrincipal.newDroniUserFromPrincipal(NEW_USER_REFRESH_TOKEN))
                .thenReturn(DroniUser.builder()
                        .email("testEmail")
                        .phoneNumber("EMPTY_NUMBER")
                        .oauthId(NEW_USER_OAUTH_ID)
                        .refreshToken(NEW_USER_REFRESH_TOKEN)
                        .build()
                );
        Optional<DroniUser> droniUserByOauthId = userQuerydslRepository.findDroniUserByOauthId(NEW_USER_OAUTH_ID);
        DroniUser newUser = userQuerydslRepository.updateWithPrincipal(NEW_USER_REFRESH_TOKEN, mockPrincipal);
        // then
        Assertions.assertTrue(droniUserByOauthId.isEmpty());
        assertThat(newUser.getRefreshToken()).isEqualTo(NEW_USER_REFRESH_TOKEN);
        assertThat(newUser.getOauthId()).isEqualTo(NEW_USER_OAUTH_ID);

    }

    @TestConfiguration
    static class TestQueryDslConfig {

        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(entityManager);
        }

        @Bean
        public DroniUserQuerydslRepository droniUserQuerydslRepository() {
            return new DroniUserQuerydslRepository(jpaQueryFactory(), null, entityManager);
        }

    }
}