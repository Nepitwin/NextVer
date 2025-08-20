package com.asekulsk.nextver.security.config

import com.asekulsk.nextver.security.filter.JwtAuthenticationFilter
import com.asekulsk.nextver.security.service.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {

    private final JwtService jwtService

    SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        new JwtAuthenticationFilter(jwtService)
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {
        http.csrf { it.disable() }
                .authorizeHttpRequests { authz ->
                    authz
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().authenticated()
                }
                .sessionManagement { sm ->
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter)

        return http.build()
    }
}
