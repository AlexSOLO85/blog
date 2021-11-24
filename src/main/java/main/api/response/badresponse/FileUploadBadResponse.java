package main.api.response.badresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class FileUploadBadResponse {
    private boolean result;
    private Errors errors;

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Component
    public static class Errors {
        private String image;
        private String imageExtension;
    }
}
