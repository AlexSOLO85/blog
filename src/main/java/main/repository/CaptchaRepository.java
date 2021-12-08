package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface CaptchaRepository extends
        PagingAndSortingRepository<CaptchaCode, Long> {
    @Transactional
    CaptchaCode deleteByTimeBefore(LocalDateTime localDateTime);
    CaptchaCode getBySecretCodeEquals(String secretCode);
}
