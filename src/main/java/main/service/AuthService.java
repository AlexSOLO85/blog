package main.service;

import main.api.response.AuthResponse;
import org.springframework.stereotype.Service;

/**
 * The type Auth service.
 */
@Service
public class AuthService {
    /**
     * Gets auth check.
     *
     * @return the auth check
     */
    public AuthResponse getAuthCheck() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(false);
        return authResponse;
    }
}
