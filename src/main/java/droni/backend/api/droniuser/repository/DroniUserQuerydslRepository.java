package droni.backend.api.droniuser.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.entity.QDroniUser;
import droni.backend.oauth2.jwt.TokenRefreshDto;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DroniUserQuerydslRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final AuthTokenProvider tokenProvider;
    private final EntityManager em;


    private final QDroniUser droniUser = QDroniUser.droniUser;

    public Optional<DroniUser> findDroniUserByOauthId(String oauth2Id) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser).where(droniUser.oauthId.eq(oauth2Id)).fetchFirst());
    }

    public Optional<DroniUser> findDroniUserByOauthId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser).where(droniUser.oauthId.eq(userDetails.getUsername())).fetchFirst());
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

    public DroniUser updateWithPrincipal(String newRefreshToken, OAuth2UserPrincipal userPrincipal) {

        Optional<DroniUser> optinalUser = Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser)
                .where(droniUser.oauthId.eq(userPrincipal.getOAuth2Id()))
                .fetchFirst()
        );
        if (optinalUser.isPresent()) {
            DroniUser existUser = optinalUser.get();
            existUser.updateRefreshToken(newRefreshToken);
            return existUser;
        } else {
            DroniUser newUser = userPrincipal.newDroniUserFromPrincipal(newRefreshToken);
            em.persist(newUser);
            return newUser;
        }

    }
}
