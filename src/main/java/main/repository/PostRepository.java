package main.repository;

import main.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query(
            value = "SELECT COUNT(posts.id) "
                    + "FROM posts "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'ACCEPTED'"
                    + "AND posts.time < NOW()",
            nativeQuery = true)
    int countAllPosts();

    @Query(
            value = "SELECT COUNT(posts.id) "
                    + "FROM posts "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'NEW'",
            nativeQuery = true)
    int countModeratePosts();

    @Query(
            value = "SELECT *"
                    + "FROM posts "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'ACCEPTED'"
                    + "AND posts.time < NOW()"
                    + "ORDER BY posts.time DESC "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> recentPosts(int offset, int limit);

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
                    + "AND posts.moderation_status = 'ACCEPTED'"
                    + "AND posts.time < NOW()"
                    + "ORDER BY sum_values DESC "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> bestPosts(int offset, int limit);

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
                    + "AND posts.moderation_status = 'ACCEPTED' "
                    + "AND posts.time < NOW() "
                    + "ORDER BY post_counts DESC "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> popularPosts(int offset, int limit);

    @Query(
            value = "SELECT *"
                    + "FROM posts "
                    + "WHERE posts.is_active = TRUE "
                    + "AND posts.moderation_status = 'ACCEPTED'"
                    + "AND posts.time < NOW()"
                    + "ORDER BY posts.time "
                    + "LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> earlyPosts(int offset, int limit);

    @Query(
            value = "SELECT DISTINCT * "
                    + "FROM posts p "
                    + "WHERE (p.text LIKE CONCAT('%',?3,'%')"
                    + "OR p.title LIKE CONCAT('%',?3,'%')) "
                    + "AND p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.time < NOW() "
                    + "ORDER BY p.time "
                    + "DESC LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> searchPosts(int offset, int limit, String query);

    @Query(
            value = "SELECT COUNT(searched_posts.id) "
                    + "FROM "
                    + "(SELECT DISTINCT * FROM posts p "
                    + "WHERE (p.text LIKE CONCAT('%',?1,'%')"
                    + "OR p.title LIKE CONCAT('%',?1,'%')) "
                    + "AND p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.time < NOW()) AS searched_posts",
            nativeQuery = true)
    int countSearchedPosts(String query);

    @Query(
            value = "SELECT * "
                    + "FROM posts p  "
                    + "WHERE YEAR(p.time) = ? "
                    + "AND p.is_active = TRUE "
                    + "AND p.moderation_status = 'ACCEPTED'"
                    + "AND p.time < NOW()",
            nativeQuery = true)
    List<Post> getPostsByYear(int year);

    @Query(
            value = "SELECT DISTINCT "
                    + "YEAR(p.time) AS post_year "
                    + "FROM posts p "
                    + "WHERE p.is_active = TRUE "
                    + "AND p.moderation_status = 'ACCEPTED'"
                    + "AND p.time < NOW()"
                    + "ORDER BY post_year DESC",
            nativeQuery = true)
    List<Integer> getYearsWithAnyPosts();

    @Query(
            value = "SELECT * "
                    + "FROM posts p "
                    + "WHERE DATEDIFF(p.time, ?) = 0 "
                    + "AND p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.time < NOW() "
                    + "ORDER BY p.time "
                    + "DESC LIMIT ? OFFSET ?",
            nativeQuery = true)
    List<Post> getPostsByDate(String date, int limit, int offset);

    @Query(
            value =
                    "SELECT COUNT(searched_posts.id) "
                            + "FROM "
                            + "(SELECT * FROM posts p "
                            + "WHERE DATEDIFF(p.time, ?) = 0 "
                            + "AND p.is_active = 1 "
                            + "AND p.moderation_status = 'ACCEPTED' "
                            + "AND p.time < NOW()) AS searched_posts",
            nativeQuery = true)
    int countPostsByDate(String dateString);

    @Query(
            value = "SELECT DISTINCT p.* "
                    + "FROM posts AS p "
                    + "INNER JOIN tag2post t2 "
                    + "ON p.id = t2.post_id "
                    + "INNER JOIN tags t "
                    + "ON t.id  = t2.tag_id "
                    + "WHERE (t.name LIKE CONCAT('%',?1,'%')) "
                    + "AND p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.time < NOW() "
                    + "ORDER BY p.time DESC "
                    + "LIMIT ?2 OFFSET ?3",
            nativeQuery = true)
    List<Post> getPostsByTag(String tag, int limit, int offset);

    @Query(
            value = "SELECT COUNT(searched_posts.id) "
                    + "FROM (SELECT DISTINCT p.* "
                    + "FROM posts AS p "
                    + "INNER JOIN tag2post t2 "
                    + "ON p.id = t2.post_id "
                    + "INNER JOIN tags t "
                    + "ON t.id  = t2.tag_id "
                    + "WHERE (t.name LIKE CONCAT('%',?1,'%')) "
                    + "AND p.is_active = 1 "
                    + "AND p.moderation_status = 'ACCEPTED' "
                    + "AND p.time < NOW()) AS searched_posts",
            nativeQuery = true)
    int countPostsByTag(String tag);

    @Query(
            value = "SELECT *"
                    + "FROM posts "
                    + "WHERE posts.moderation_status = 'ACCEPTED'"
                    + "AND posts.time < NOW()"
                    + "AND posts.id = ?1",
            nativeQuery = true)
    Post findByIdPost(int id);

    @Query(
            value =
                    "SELECT * "
                            + "FROM posts p "
                            + "WHERE p.user_id = ?4 "
                            + "AND p.is_active = 1 "
                            + "AND p.moderation_status = ?3 "
                            + "ORDER BY p.time "
                            + "DESC LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> getMyActivePosts(int offset, int limit,
                                String status, int userId);

    @Query(
            value
                    = "SELECT COUNT(searched_posts.id) "
                    + "FROM (SELECT * FROM posts p "
                    + "WHERE p.user_id = ?2 "
                    + "AND p.is_active = 1 "
                    + "AND p.moderation_status = ?1) "
                    + "AS searched_posts",
            nativeQuery = true)
    int countMyActivePosts(String moderationStatus, int id);

    @Query(
            value =
                    "SELECT * "
                            + "FROM posts p "
                            + "WHERE p.user_id = ?3 "
                            + "AND p.is_active = 0 "
                            + "ORDER BY p.time "
                            + "DESC LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> getMyNotActivePosts(int offset, int limit, int userId);

    @Query(
            value =
                    "SELECT COUNT(searched_posts.id) "
                            + "FROM (SELECT * FROM posts p "
                            + "WHERE p.user_id = ?1 "
                            + "AND p.is_active = 0) "
                            + "AS searched_posts",
            nativeQuery = true)
    int countMyNotActivePosts(int id);

    @Query(
            value =
                    "SELECT * "
                            + "FROM posts p "
                            + "WHERE p.is_active = 1 "
                            + "AND p.moderation_status = 'NEW' "
                            + "ORDER BY p.time "
                            + "DESC LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> getPostsForModeration(int offset, int limit);

    @Query(
            value =
                    "SELECT COUNT(p.id) "
                            + "FROM posts p "
                            + "WHERE p.is_active = 1 "
                            + "AND p.moderation_status = 'NEW'",
            nativeQuery = true)
    int countPostsForModeration();

    @Query(
            value =
                    "SELECT * "
                            + "FROM posts p "
                            + "WHERE p.moderator_id = ?4 "
                            + "AND p.is_active = 1 "
                            + "AND p.moderation_status = ?3 "
                            + "ORDER BY p.time "
                            + "DESC LIMIT ?2 OFFSET ?1",
            nativeQuery = true)
    List<Post> getPostsModeratedByMe(int offset, int limit,
                                     String status, int moderatorId);

    @Query(
            value =
                    "SELECT COUNT(searched_posts.id) "
                            + "FROM (SELECT * FROM posts p "
                            + "WHERE p.moderator_id = ?2 "
                            + "AND p.is_active = 1 "
                            + "AND p.moderation_status = ?1) "
                            + "AS searched_posts",
            nativeQuery = true)
    int countPostsModeratedByMe(String moderationStatus, int id);
}
