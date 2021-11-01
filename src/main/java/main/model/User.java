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
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type User.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "users")
public class User {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * The Is moderator.
     */
    @Column(name = "is_moderator", nullable = false)
    private Boolean isModerator;
    /**
     * The Reg time.
     */
    @Column(name = "reg_time", nullable = false)
    private LocalDateTime regTime;
    /**
     * The Name.
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * The Email.
     */
    @Column(name = "email", nullable = false)
    private String email;
    /**
     * The Password.
     */
    @Column(name = "password", nullable = false)
    private String password;
    /**
     * The Code.
     */
    @Column(name = "code")
    private String code;
    /**
     * The Photo.
     */
    @Column(name = "photo")
    private String photo;
    /**
     * The Posts.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Post> posts;
    /**
     * The Post votes.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<PostVote> postVotes;
    /**
     * The Post comments.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<PostComment> postComments;
    /**
     * The Posts moderated.
     */
    @OneToMany(mappedBy = "moderatorId", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Post> postsModerated;
}
