package com.coffeeshop.coffee_shop_backend.config;

import com.coffeeshop.coffee_shop_backend.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                .requestMatchers("/api/auth/**").permitAll()

                                // Manager-Only Endpoints
                                .requestMatchers("/api/menu/**").hasAnyRole("MANAGER","BARISTA")
//                        .requestMatchers("/api/inventory/**").hasRole("MANAGER")
                                .requestMatchers("/api/reports/**").hasRole("MANAGER")

                                //Manager and Chef(view) Endpoints
                                .requestMatchers(HttpMethod.GET, "/api/inventory/**").hasAnyRole("MANAGER", "CHEF")
                                .requestMatchers(HttpMethod.PUT, "/api/inventory/**").hasAnyRole("MANAGER", "CHEF")

                                .requestMatchers(HttpMethod.POST, "/api/inventory/**").hasRole("MANAGER")

                                // Barista + Manager Endpoints
                                .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyRole("MANAGER", "BARISTA")
                                .requestMatchers("/api/orders/**").hasAnyRole("MANAGER", "BARISTA","CHEF")

                                // Any other request must be authenticated
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}