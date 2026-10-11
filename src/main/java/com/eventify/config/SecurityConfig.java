package com.eventify.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Real authentication now comes from DatabaseUserDetailsService (checks
    // the Attendee/Organizer/Admin tables directly) - Spring Boot picks it up
    // automatically since it's the only UserDetailsService bean in the app.

    @Bean
    PasswordEncoder passwordEncoder() {
        // Supports multiple encoding schemes via a "{id}" prefix on the stored
        // value - we currently use "{noop}" (plain text comparison) to match
        // what's actually in the DB today, while leaving room to switch a
        // user over to "{bcrypt}" later without touching any other code.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:3000")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(withDefaults())

            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/eventify/auth/**").permitAll()
                    // Anyone can browse the public event list/details without logging in.
                    .requestMatchers(HttpMethod.GET, "/eventify/event", "/eventify/event/*").permitAll()
                    .anyRequest().authenticated()
            )

            .httpBasic(withDefaults());

        return http.build();
    }
   
}