package com.project.insurtech.configurations;

import com.project.insurtech.entities.Role;
import com.project.insurtech.entities.User;
import com.project.insurtech.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    String.format("%s/products/count-by-provider", apiPrefix),
                                    String.format("%s/products/list", apiPrefix),
                                    String.format("%s/products/{id}", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(
                                    POST, String.format("%s/files/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    PUT, String.format("%s/users", apiPrefix)).permitAll()

                            .requestMatchers(
                                    GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    POST, String.format("%s/categories/**", apiPrefix)).hasRole(Role.PROVIDER)
                            .requestMatchers(
                                    PUT, String.format("%s/categories/**", apiPrefix)).hasRole(Role.PROVIDER)
                            .requestMatchers(
                                    DELETE, String.format("%s/categories/**", apiPrefix)).hasRole(Role.PROVIDER)

                            .requestMatchers(
                                    GET, String.format("%s/products", apiPrefix)).permitAll()
                            .requestMatchers(
                                    POST, String.format("%s/products/**", apiPrefix)).hasRole(Role.PROVIDER)
                            .requestMatchers(
                                    PUT, String.format("%s/products/**", apiPrefix)).hasRole(Role.PROVIDER)
                            .requestMatchers(
                                    DELETE, String.format("%s/products/**", apiPrefix)).hasRole(Role.PROVIDER)

                            //API mobile
                            .requestMatchers(
                                    POST, String.format("%s/admin/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    GET, String.format("%s/admin/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    PUT, String.format("%s/admin/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    DELETE, String.format("%s/admin/**", apiPrefix)).permitAll()

                            .requestMatchers(
                                    POST, String.format("%s/providers/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(
                                    GET, String.format("%s/providers/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    PUT, String.format("%s/providers/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    DELETE, String.format("%s/providers/**", apiPrefix)).permitAll()

                            //Contract
                            .requestMatchers(
                                    POST, String.format("%s/contracts", apiPrefix)).hasRole(Role.USER)

                            .requestMatchers(
                                    GET, String.format("%s/contracts/provider", apiPrefix)).hasRole(Role.PROVIDER)
                            //User
                            .requestMatchers(
                                    GET, String.format("%s/contracts/user", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(
                                    GET, String.format("%s/contracts/{contractId}", apiPrefix)).hasAnyRole(Role.USER, Role.PROVIDER)
                            .requestMatchers(
                                    GET, String.format("%s/users/{id}", apiPrefix)).permitAll()

                            //Web
                            .requestMatchers(
                                    GET, String.format("%s/provider/", apiPrefix)).hasRole(Role.PROVIDER)

                            //Claims
                            .requestMatchers(
                                    POST, String.format("%s/claims", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(
                                    GET, String.format("%s/claims/user", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(
                                    GET, String.format("%s/claims/provider", apiPrefix)).hasRole(Role.PROVIDER)
                            .requestMatchers(
                                    GET, String.format("%s/claims/provider/{claimId}", apiPrefix)).hasRole(Role.PROVIDER)
                            .requestMatchers(
                                    PUT, String.format("%s/claims/provider/{claimId}", apiPrefix)).hasRole(Role.PROVIDER)

                            .anyRequest().authenticated();
                });
        return http.build();
    }

}
