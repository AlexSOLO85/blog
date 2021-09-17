package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import main.model.enumerated.ModerationStatus;

import javax.persistence.*;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "moderation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ModerationStatus moderationStatus;

    @Column(name = "moderator_id")
    private Long moderatorId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    @OneToMany(mappedBy = "post")
    @JsonIgnore
    @ToString.Exclude
    private List<PostVotes> postVotes;

    @OneToMany(mappedBy = "parentId")
    @JsonIgnore
    @ToString.Exclude
    private List<PostComments> postComments;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "posts")
    private List<Tags> tags;
}