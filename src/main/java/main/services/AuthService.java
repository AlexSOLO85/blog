package main.services;

import main.api.response.BooleanResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public final ResponseEntity<BooleanResponse> getAuthCheck() {
        return new ResponseEntity<>(new BooleanResponse(false),
                HttpStatus.OK);
    }
}
