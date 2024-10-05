package com.project.insurtech.configurations;

import com.project.insurtech.entities.Role;
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
                                    String.format("%s/users/login", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(
                                    GET, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    POST, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    PUT, String.format("%s/categories/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    DELETE, String.format("%s/categories/**", apiPrefix)).permitAll()

                            .requestMatchers(
                                    GET, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    POST, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    PUT, String.format("%s/products/**", apiPrefix)).permitAll()
                            .requestMatchers(
                                    DELETE, String.format("%s/products/**", apiPrefix)).permitAll()

                            .anyRequest().authenticated();
                });
        return http.build();
    }

}
