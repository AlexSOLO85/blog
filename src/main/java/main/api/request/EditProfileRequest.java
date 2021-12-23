package main.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EditProfileRequest {
    private Byte removePhoto;
    private String name;
    private String email;
    private String password;
}
