package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class EditProfileRequest {
    private Byte removePhoto;
    private String name;
    private String email;
    private String password;
}
