package droni.backend.oauth2.jwt;

import droni.backend.oauth2.execption.JWTException;
import droni.backend.oauth2.token.AuthToken;
import droni.backend.oauth2.token.AuthTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static droni.backend.config.security.SecurityConfig.ARROWED_APIS;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.resolveToken(request);
        try {
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                AuthToken authToken = tokenProvider.convertToAuthToken(token);
                Authentication authentication = tokenProvider.getAuthentication(authToken.getToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JWTException e) {
            if (isAuthenticatedRequest(request)) {
                throw e;
            }
        }
        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        String BEARER_PREFIX = "Bearer ";
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private boolean isAuthenticatedRequest(HttpServletRequest request) {
        return Arrays.stream(ARROWED_APIS).parallel()
                .noneMatch(antPathRequestMatcher -> antPathRequestMatcher.matches(request));
    }
}
