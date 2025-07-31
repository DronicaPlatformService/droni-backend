package droni.backend.api.droniuser.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.api.droniuser.entity.QDroniUser;
import droni.backend.api.droniuser.exception.DroniUserException;
import droni.backend.oauth2.service.OAuth2UserPrincipal;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DroniUserQuerydslRepositoryImpl implements DroniUserQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;
    private final QDroniUser droniUser = QDroniUser.droniUser;

    @Override
    public Optional<DroniUser> findByUserOauth2Id(String oauth2Id) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(droniUser).where(oauth2IdEq(oauth2Id)).fetchFirst());
    }

    @Override
    public DroniUser findUserFromContextHolder() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || Objects.isNull(authentication.getPrincipal())) {
            throw new DroniUserException(HttpStatus.UNAUTHORIZED,
                    "Authentication required or token for testing given");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<DroniUser> findDroniUser = Optional.ofNullable(jpaQueryFactory
                .selectFrom(droniUser).where(oauth2IdEq(userDetails.getUsername())).fetchFirst());

        return findDroniUser.orElseThrow(
                () -> new DroniUserException(HttpStatus.BAD_REQUEST, "Login user not found"));
    }

    @Override
    public Optional<DroniUser> findReissueUser(String oauth2Id, String refreshToken) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser)
                .where(oauth2IdEq(oauth2Id), refreshTokenEq(refreshToken)).fetchFirst());
    }

    private BooleanExpression refreshTokenEq(String refreshToken) {
        return droniUser.refreshToken.eq(refreshToken);
    }

    private BooleanExpression oauth2IdEq(String oauth2Id) {
        return droniUser.oauthId.eq(oauth2Id);
    }

    @Override
    public DroniUser updateWithPrincipal(String newRefreshToken,
            OAuth2UserPrincipal userPrincipal) {
        Optional<DroniUser> optionalUser = Optional.ofNullable(jpaQueryFactory.selectFrom(droniUser)
                .where(oauth2IdEq(userPrincipal.getOAuth2Id())).fetchFirst());

        if (optionalUser.isPresent()) {
            DroniUser existUser = optionalUser.get();
            existUser.updateRefreshToken(newRefreshToken);
            return existUser;
        } else {
            DroniUser newUser = userPrincipal.newDroniUserFromPrincipal(newRefreshToken);
            em.persist(newUser);
            return newUser;
        }
    }

    @Override
    public Optional<DroniUser> findByOauthId(String oauthId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(droniUser).where(oauth2IdEq(oauthId)).fetchFirst());
    }
}
