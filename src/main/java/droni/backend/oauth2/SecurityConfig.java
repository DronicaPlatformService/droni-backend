package droni.backend.oauth2;

import droni.backend.oauth2.handler.DroniOAuth2AuthSuccessHandler;
import droni.backend.oauth2.jwt.JwtAuthorizationFilter;
import droni.backend.oauth2.service.DroniOAuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DroniOAuthUserService droniOAuthUserService;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final DroniCookieAuthorizationRequestRepository droniCookieAuthorizationRequestRepository;
    private final DroniOAuth2AuthSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(
                        conigurer -> conigurer.userInfoEndpoint(config -> config.userService(droniOAuthUserService))
                                .authorizationEndpoint(config -> config.authorizationRequestRepository(droniCookieAuthorizationRequestRepository))
                                .successHandler(successHandler)
                );
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
