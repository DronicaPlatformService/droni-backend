package droni.backend.api.droniuser.repository;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.oauth2.service.OAuth2UserPrincipal;

import java.util.Optional;

public interface DroniUserQuerydslRepository {
    Optional<DroniUser> findByUserOauth2Id(String oauth2Id);

    DroniUser findUserFromContextHolder();

    Optional<DroniUser> findReissueUser(String expiredAccessToken, String refreshToken);

    DroniUser updateWithPrincipal(String newRefreshToken, OAuth2UserPrincipal userPrincipal);
}
