package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface CaptchaRepository extends
        PagingAndSortingRepository<CaptchaCode, Long> {
    @Transactional
    @Modifying
    @Query(
            value =
                    "DELETE "
                            + "FROM captcha_codes "
                            + "WHERE time < ?",
            nativeQuery = true)
    void deleteOldCaptcha(LocalDateTime captchaDeletedBeforeTime);

    @Query(
            value =
                    "SELECT * "
                            + "FROM captcha_codes c "
                            + "WHERE c.secret_code = ?",
            nativeQuery = true)
    CaptchaCode getCaptchaBySecretCode(String secretCode);
}
