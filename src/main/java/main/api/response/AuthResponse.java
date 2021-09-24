package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * The type Auth response.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponse {
    /**
     * The Result.
     */
    private boolean result;
}
