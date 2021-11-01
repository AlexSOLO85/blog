package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;

/**
 * The type Captcha codes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * The Time.
     */
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
    /**
     * The Code.
     */
    @Column(name = "code", nullable = false)
    private String code;
    /**
     * The Secret code.
     */
    @Column(name = "secret_code", nullable = false)
    private String secretCode;
}
