package main.api.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
@Component
public class EditProfilePhotoRequest extends EditProfileRequest {
    private MultipartFile photo;
    public EditProfilePhotoRequest(final Byte removePhoto,
                                   final String name,
                                   final String email,
                                   final String password,
                                   final MultipartFile photoParam) {
        super(removePhoto, name, email, password);
        this.photo = photoParam;
    }
}
