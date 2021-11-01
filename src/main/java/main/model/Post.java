package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import main.model.enumerated.ModerationStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Post.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "posts")
public class Post {
    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    /**
     * The Is active.
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    /**
     * The Moderation status.
     */
    @Column(name = "moderation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus;
    /**
     * The Moderator id.
     */
    @Column(name = "moderator_id")
    private Long moderatorId;
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
     * The Title.
     */
    @Column(name = "title", nullable = false)
    private String title;
    /**
     * The Text.
     */
    @Column(name = "text", nullable = false)
    private String text;
    /**
     * The View count.
     */
    @Column(name = "view_count", nullable = false)
    private Integer viewCount;
    /**
     * The Post votes.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<PostVote> postVotes;
    /**
     * The Post comments.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<PostComment> postComments;
    /**
     * The Tag to posts.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<TagToPost> tagsToPosts;
}
