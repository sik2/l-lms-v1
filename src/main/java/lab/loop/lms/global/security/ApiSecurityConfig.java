package lab.loop.lms.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {

    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/api/*/members/login", "/api/*/members/logout").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(
                        csrf -> csrf.disable()
                )
                .sessionManagement(
                        sessionManagement -> sessionManagement.disable()
                )
                .cors(
                        cors -> cors.configure(http)
                );
        return http.build();
    }
}