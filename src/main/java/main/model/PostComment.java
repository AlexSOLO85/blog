package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Post comments.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "post_comments")
public class PostComment {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * The Parent id.
     */
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private PostComment parentId;
    /**
     * The Post.
     */
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    /**
     * The User.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    /**
     * The Time.
     */
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
    /**
     * The Text.
     */
    @Column(name = "text", nullable = false)
    private String text;
    /**
     * The Child post comments.
     */
    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    private List<PostComment> childPostComments;
}
