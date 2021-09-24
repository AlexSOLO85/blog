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

/**
 * The type Global settings.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "global_settings")
public class GlobalSettings {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * The Code.
     */
    @Column(name = "code", nullable = false)
    private String code;

    /**
     * The Name.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The Value.
     */
    @Column(name = "value", nullable = false)
    private String value;
}
