package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends BooleanResponse {
    @JsonProperty("user")
    private UserLoginResponse userLoginResponse;

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class UserLoginResponse {
        private int id;
        private String name;
        private String photo;
        private String email;
        private boolean moderation;
        private int moderationCount;
        private boolean settings;
    }
}
