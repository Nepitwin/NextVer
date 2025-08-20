package com.asekulsk.nextver.security.controller

import com.asekulsk.nextver.security.service.JwtService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController {

    private final JwtService jwtService

    AuthController(JwtService jwtService) {
        this.jwtService = jwtService
    }

    @PostMapping("/login")
    String login(@RequestParam String username, @RequestParam String password) {

        // This is a placeholder for actual authentication logic.
        // In a real application, you would verify the username and password against a user database.

        if (username == "user" && password == "password") {
            return jwtService.generateToken(username)
        }

        throw new RuntimeException("Invalid Login")
    }
}
