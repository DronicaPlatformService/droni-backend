package droni.backend.api.droniuser.repository;

import backend.generated_model.TokenRefreshDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.droniuser.entity.DroniUser;

import droni.backend.api.droniuser.entity.QDroniUser;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponents;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DroniUserQuerydslRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final AuthTokenProvider tokenProvider;
    private final EntityManager em;


    private final QDroniUser droniUser = QDroniUser.droniUser;

    public Optional<DroniUser> findDroniUserByOauthId(String oauth2Id) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser).where(droniUser.oauthId.eq(oauth2Id)).fetchFirst());
    }

    public Optional<DroniUser> findRequestedUser(TokenRefreshDto prevToken) {
        AuthToken requestAccessToken = tokenProvider.convertToAuthToken(prevToken.getAccessToken());
        AuthToken requestRefreshToken = tokenProvider.convertToAuthToken(prevToken.getRefreshToken());
        return  Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser)
                .where(
                        droniUser.oauthId.eq(requestAccessToken.getSubjectFromExpiredJwt())
                                .and(droniUser.refreshToken.eq(requestRefreshToken.getToken()))
                ).fetchFirst());
    }

    public DroniUser upateWithPricipal(UriComponents tokenUriComponents, OAuth2UserPrincipal userPrincipal) {
        String refreshToken = tokenUriComponents.getQueryParams().get("refresh_token").toString();
        Optional<DroniUser> optinalUser = Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser)
                .where(droniUser.oauthId.eq(userPrincipal.getOAuth2Id()))
                .fetchFirst()
        );
        if (optinalUser.isPresent()) {
            DroniUser existUser = optinalUser.get();
            existUser.updateRefreshToken(refreshToken);
            return existUser;
        } else {
            DroniUser newUser = userPrincipal.newDroniUserFromPrincipal(refreshToken);
            em.persist(newUser);
            return newUser;
        }

    }



}
