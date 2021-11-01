package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.List;

/**
 * The type Tag.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "tags")
public class Tag {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * The Name.
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * The Tag to posts.
     */
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<TagToPost> tagsToPosts;
}
