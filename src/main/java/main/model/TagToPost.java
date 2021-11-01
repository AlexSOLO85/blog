package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The type Tag to post.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "tag2post")
public class TagToPost {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * The Tag.
     */
    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
    /**
     * The Post.
     */
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
