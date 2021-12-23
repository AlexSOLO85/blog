package main.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import main.enumerated.Role;
import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NonNull
    @Column(name = "is_moderator", nullable = false)
    private Boolean isModerator;
    @NonNull
    @Column(name = "reg_time", nullable = false)
    private LocalDateTime regTime;
    @NonNull
    @Column(name = "name", nullable = false)
    private String name;
    @NonNull
    @Column(name = "email", nullable = false)
    private String email;
    @NonNull
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "code")
    private String code;
    @NonNull
    @Column(name = "photo")
    private String photo;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Post> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<PostVote> postVotes;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<PostComment> postComments;
    @OneToMany(mappedBy = "moderatorId", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Post> postsModerated;

    public final Role getRole() {
        return Boolean.TRUE.equals(isModerator) ? Role.MODERATOR : Role.USER;
    }

    public final boolean getAuthUser() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .isAuthenticated();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
