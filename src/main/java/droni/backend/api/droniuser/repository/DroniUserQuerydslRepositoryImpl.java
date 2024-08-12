package droni.backend.api.droniuser.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.entity.QDroniUser;
import droni.backend.api.droniuser.exception.DroniUserException;
import droni.backend.oauth2.jwt.TokenRefreshDto;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DroniUserQuerydslRepositoryImpl implements DroniUserQuerydslRepository{
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;


    private final QDroniUser droniUser = QDroniUser.droniUser;

    @Override
    public Optional<DroniUser> findByUserOauth2Id(String oauth2Id) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser).where(droniUser.oauthId.eq(oauth2Id)).fetchFirst());
    }

    @Override
    public DroniUser findRequestUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<DroniUser> droniUser1 = Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser).where(droniUser.oauthId.eq(userDetails.getUsername())).fetchFirst());
        return droniUser1.orElseThrow(() -> new DroniUserException(HttpStatus.BAD_REQUEST, "Login user not found"));
    }

    @Override
    public Optional<DroniUser> findReissueUser(String expiredAccessToken, String refreshToken) {
        return  Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser)
                .where(
                        droniUser.oauthId.eq(expiredAccessToken)
                                .and(droniUser.refreshToken.eq(refreshToken))
                ).fetchFirst());
    }

    @Override
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
