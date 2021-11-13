package main.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.CaptchaCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Component
public class CaptchaDTO {
    public final CaptchaCode captchaCodeToEntity(final LocalDateTime now,
                                                 final String token,
                                                 final String secretCode) {
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(now);
        captchaCode.setCode(token);
        captchaCode.setSecretCode(secretCode);
        return captchaCode;
    }
}
