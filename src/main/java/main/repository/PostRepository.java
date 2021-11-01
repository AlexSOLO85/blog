package main.repository;

import main.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * The interface Post repository.
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    /**
     * Recent post list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    @Query(
            value = "SELECT *"
                    + "FROM posts "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'NEW'"
                    + "AND posts.time < NOW()"
                    + "ORDER BY posts.time DESC "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> recentPost(int offset, int limit);

    /**
     * Best post list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    @Query(
            value = "SELECT *"
                    + "FROM posts "
                    + "LEFT JOIN "
                    + "(SELECT post_votes.post_id, "
                    + "SUM(post_votes.value) AS sum_values "
                    + "FROM post_votes "
                    + "GROUP BY post_votes.post_id) AS sum_votes "
                    + "ON posts.id = sum_votes.post_id "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'NEW'"
                    + "AND posts.time < NOW()"
                    + "ORDER BY sum_values DESC "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> bestPost(int offset, int limit);

    /**
     * Popular post list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    @Query(
            value = "SELECT *"
                    + "FROM posts "
                    + "LEFT JOIN "
                    + "(SELECT post_comments.post_id,"
                    + "COUNT(post_comments.post_id) AS post_counts "
                    + "FROM post_comments "
                    + "GROUP BY post_comments.post_id) AS counts "
                    + "ON posts.id = counts.post_id "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'NEW' "
                    + "AND posts.time < NOW() "
                    + "ORDER BY post_counts DESC "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> popularPost(int offset, int limit);

    /**
     * Early post list.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    @Query(
            value = "SELECT *"
                    + "FROM posts "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'NEW'"
                    + "AND posts.time < NOW()"
                    + "ORDER BY posts.time "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> earlyPost(int offset, int limit);
}
