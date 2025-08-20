package com.asekulsk.nextver.security.filter

import com.asekulsk.nextver.security.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService

    JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        def header = request.getHeader("Authorization")

        if (header?.startsWith("Bearer ")) {
            def token = header.substring(7)
            if (jwtService.validateToken(token)) {
                def username = jwtService.extractUsername(token)

                def authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        [new SimpleGrantedAuthority("ROLE_USER")]
                )

                SecurityContextHolder.context.authentication = authentication
            }
        }

        chain.doFilter(request, response)
    }
}