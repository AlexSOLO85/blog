package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface CaptchaRepository extends
        PagingAndSortingRepository<CaptchaCode, Long> {
    @Query("delete from CaptchaCode c "
            + "where c.time < :localDateTime")
    @Modifying
    @Transactional
    int deleteByTimeBefore(@Param("localDateTime") LocalDateTime localDateTime);
    CaptchaCode getBySecretCodeEquals(String secretCode);
}
