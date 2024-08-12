package droni.backend.api.droniuser.repository;

import droni.backend.api.droniuser.entity.DroniUser;
import droni.backend.oauth2.jwt.TokenRefreshDto;
import droni.backend.oauth2.service.OAuth2UserPrincipal;

import java.util.Optional;

public interface DroniUserQuerydslRepository {
    Optional<DroniUser> findByUserOauth2Id(String oauth2Id);

    DroniUser findRequestUserFromContext();

    Optional<DroniUser> findRequestedUser(TokenRefreshDto prevToken);

    DroniUser updateWithPrincipal(String newRefreshToken, OAuth2UserPrincipal userPrincipal);
}
