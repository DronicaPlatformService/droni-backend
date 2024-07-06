package droni.backend.config.security;

import droni.backend.oauth2.DroniCookieAuthorizationRequestRepository;
import droni.backend.oauth2.handler.DroniOAuth2AuthFailureHandler;
import droni.backend.oauth2.handler.DroniOAuth2AuthSuccessHandler;
import droni.backend.oauth2.jwt.JwtAuthEntryPoint;
import droni.backend.oauth2.jwt.TokenAuthenticationFilter;
import droni.backend.oauth2.jwt.TokenExceptionFilter;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DroniOAuthUserService droniOAuthUserService;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final TokenExceptionFilter tokenExceptionFilter;
    private final DroniCookieAuthorizationRequestRepository droniCookieAuthorizationRequestRepository;
    private final DroniOAuth2AuthSuccessHandler successHandler;
    private final DroniOAuth2AuthFailureHandler failureHandler;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    public static final AntPathRequestMatcher[] ARROWED_APIS = new AntPathRequestMatcher[]{
            antMatcher("/reissue"),
            antMatcher("/favicon.ico"),
            antMatcher("/swagger-ui/**"),
            antMatcher("/openapi/doc/**")
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(ARROWED_APIS).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling((exceptionConfig) -> exceptionConfig.authenticationEntryPoint(jwtAuthEntryPoint))
                .oauth2Login(
                        conigurer -> conigurer.userInfoEndpoint(config -> config.userService(droniOAuthUserService))
                                .authorizationEndpoint(config -> config.authorizationRequestRepository(droniCookieAuthorizationRequestRepository))
                                .successHandler(successHandler)
                                .failureHandler(failureHandler)
                );
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenExceptionFilter, TokenAuthenticationFilter.class);
        return http.build();
    }
}
