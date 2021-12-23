package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BadResponse extends BooleanResponse {
    private Errors errors;

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Component
    public static class Errors {
        private String code;
        private String password;
        private String captcha;
        private String image;
        private String imageExtension;
        private String title;
        private String text;
        private String email;
        private String name;
    }
}
