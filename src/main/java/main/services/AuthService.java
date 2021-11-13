package main.services;

import main.api.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public final ResponseEntity<AuthResponse> getAuthCheck() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResult(false);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
