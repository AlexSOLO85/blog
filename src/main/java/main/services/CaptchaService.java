package main.services;

import com.github.cage.Cage;
import com.github.cage.ObjectRoulette;
import com.github.cage.image.EffectConfig;
import com.github.cage.image.Painter;
import com.github.cage.image.RgbColorGenerator;
import com.github.cage.token.RandomTokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import main.api.response.CaptchaResponse;
import main.utils.SaveToEntity;
import main.model.CaptchaCode;
import main.repository.CaptchaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.Font;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CaptchaService {
    @Value("${captcha.delete_timeout}")
    private int oldCaptchaDeleteTimeInMin;
    @Value("${captcha.random_secret_key_length}")
    private int randomSecretKeyLength;
    @Value("${captcha.image.text.length}")
    private int captchaPictureTextLength;
    @Value("${captcha.image.format}")
    private String captchaFormat;
    @Value("${captcha.image.format_string}")
    private String captchaFormatString;
    @Value("${captcha.image.text.font.random_font1}")
    private String captchaImageRandomFont1;
    @Value("${captcha.image.text.font.random_font2}")
    private String captchaImageRandomFont2;
    @Value("${captcha.image.text.font.random_font3}")
    private String captchaImageRandomFont3;
    @Value("${captcha.image.width}")
    private int captchaImageWidth;
    @Value("${captcha.image.height}")
    private int captchaImageHeight;
    private static final int UNICODE_NUMERAL_0 = 48;
    private static final int UNICODE_NUMERAL_9 = 57;
    private static final int UNICODE_LETTER_Z_LOWERCASE = 122;
    private static final int UNICODE_LETTER_Z_UPPERCASE = 90;
    private static final int UNICODE_LETTER_A_UPPERCASE = 65;
    private static final int UNICODE_LETTER_A_LOWERCASE = 97;
    private final CaptchaRepository captchaRepository;
    private final SaveToEntity saveToEntity;
    private final CaptchaResponse captchaResponse;

    public final ResponseEntity<CaptchaResponse> generateCaptcha() {
        LocalDateTime captchaDeleteBeforeTime =
                LocalDateTime.now().minusMinutes(oldCaptchaDeleteTimeInMin);
        captchaRepository.deleteOldCaptcha(captchaDeleteBeforeTime);

        Cage cage = getCage();
        String secretCode = generateRandomString();
        String token = cage.getTokenGenerator().next();
        if (token.length() > captchaPictureTextLength) {
            token = token.substring(0, captchaPictureTextLength);
        }
        byte[] encodedBytes = Base64.getEncoder().encode(cage.draw(token));
        String captchaImageBase64String = captchaFormatString + ", "
                + new String(encodedBytes, StandardCharsets.UTF_8);
        CaptchaCode newCaptcha = saveToEntity
                .captchaCodeToEntity(LocalDateTime.now(), token, secretCode);
        captchaRepository.save(newCaptcha);
        captchaResponse.setSecret(secretCode);
        captchaResponse.setImage(captchaImageBase64String);
        return new ResponseEntity<>(captchaResponse, HttpStatus.OK);
    }

    @SneakyThrows
    private String generateRandomString() {
        Random random = SecureRandom.getInstanceStrong();
        return random.ints(UNICODE_NUMERAL_0, UNICODE_LETTER_Z_LOWERCASE + 1)
                .filter(i ->
                        (i <= UNICODE_NUMERAL_9
                                || i >= UNICODE_LETTER_A_UPPERCASE)
                                && (i <= UNICODE_LETTER_Z_UPPERCASE
                                || i >= UNICODE_LETTER_A_LOWERCASE))
                .limit(randomSecretKeyLength)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @SneakyThrows
    private Cage getCage() {
        Random random = SecureRandom.getInstanceStrong();
        Painter painter = new Painter(
                captchaImageWidth, captchaImageHeight,
                Color.WHITE, Painter.Quality.MAX, new EffectConfig(), random);
        int defFontHeight = painter.getHeight();
        return new Cage(
                painter,
                new ObjectRoulette<>(random,
                        new Font(captchaImageRandomFont1,
                                Font.PLAIN, defFontHeight),
                        new Font(captchaImageRandomFont2,
                                Font.PLAIN, defFontHeight),
                        new Font(captchaImageRandomFont3,
                                Font.BOLD, defFontHeight)),
                new RgbColorGenerator(random), captchaFormat,
                Cage.DEFAULT_COMPRESS_RATIO,
                new RandomTokenGenerator(random), random);
    }
}
