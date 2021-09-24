package main.repository;

import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Post repository.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Gets all post.
     *
     * @param pageable the pageable
     * @return the all post
     */
    @Query(
            value = "SELECT * FROM posts "
                    + "WHERE posts.is_active = true AND "
                    + "posts.moderation_status = 'NEW' "
                    + "AND posts.time <= CURRENT_DATE "
                    + "ORDER BY posts.id",
            countQuery = "SELECT count(posts.id) FROM posts",
            nativeQuery = true)
    List<Post> getAllPost(Pageable pageable);
}
