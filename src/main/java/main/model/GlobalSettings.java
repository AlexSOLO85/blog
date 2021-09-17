package main.model;

import lombok.*;

import javax.persistence.*;

/**
 * The type Global settings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "global_settings")

public class GlobalSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;
}