package droni.backend.droniuser.repository;

import backend.generated_model.TokenRefreshDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import droni.backend.droniuser.entity.DroniUser;
import droni.backend.droniuser.entity.QDroniUser;
import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DroniUserQuerydslRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final AuthTokenProvider tokenProvider;

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


}
