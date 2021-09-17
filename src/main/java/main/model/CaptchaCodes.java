package main.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The type Captcha codes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "captcha_codes")
public class CaptchaCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "secret_code", nullable = false)
    private String secretCode;
}