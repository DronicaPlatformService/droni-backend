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
class DroniUserQuerydslRepositoryImplTest {
    @Autowired
    private DroniUserRepository userRepository;

    @Test
    @DisplayName("유저 조회 정상 작동 테스트")
    void findRequestUserFromContext() {
        //given
        DroniUser droniUser = userRepository.findByUserOauth2Id("3497839913").orElse(null);
        //then
        org.junit.jupiter.api.Assertions.assertTrue(Objects.nonNull(droniUser));
        assertThat(droniUser.getUserId()).isEqualTo(4);
    }

    @Test
    @DisplayName("사용자 인증시 Principal 유저 업데이트 테스트")
    void updateUserPrinciple() {
        final String TEST_TOKEN = "testRefreshToken";
        //given
        OAuth2UserPrincipal mockPrincipal = mock(OAuth2UserPrincipal.class);
        //when
        when(mockPrincipal.getOAuth2Id()).thenReturn("3497839913");
        DroniUser updatedUser = userRepository.updateWithPrincipal(TEST_TOKEN, mockPrincipal);
        //then
        assertThat(updatedUser.getRefreshToken()).isEqualTo(TEST_TOKEN);
    }

    @Test
    @DisplayName("로그인 시 새로운 유저 생성 테스트")
    void saveUserWhenLoginFirst() {
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
        Optional<DroniUser> droniUserByOauthId = userRepository.findByUserOauth2Id(NEW_USER_OAUTH_ID);
        DroniUser newUser = userRepository.updateWithPrincipal(NEW_USER_REFRESH_TOKEN, mockPrincipal);
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
        public DroniUserQuerydslRepositoryImpl droniUserQuerydslRepository() {
            return new DroniUserQuerydslRepositoryImpl(jpaQueryFactory(), entityManager);
        }

    }
}