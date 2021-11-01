package main.controller;

import main.api.response.AuthResponse;
import main.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Api auth controller.
 */
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    /**
     * The Auth service.
     */
    private final AuthService authService;

    /**
     * Instantiates a new Api auth controller.
     *
     * @param authServices the auth service
     */
    public ApiAuthController(final AuthService authServices) {
        this.authService = authServices;
    }

    /**
     * Auth check response auth response.
     *
     * @return the auth response
     */
    @GetMapping("/check")
    private ResponseEntity<AuthResponse> authCheckResponse() {
        return ResponseEntity.ok(authService.getAuthCheck());
    }
}
