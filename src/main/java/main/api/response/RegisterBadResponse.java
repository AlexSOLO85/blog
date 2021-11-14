package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class RegisterBadResponse {
    private boolean result;
    private ErrorResponse errors;

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Component
    public static class ErrorResponse {
        private String email;
        private String name;
        private String password;
        private String captcha;
    }
}
