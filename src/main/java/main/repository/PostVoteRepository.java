package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostVoteRepository
        extends PagingAndSortingRepository<PostVote, Long> {
    @Query(
            value =
                    "SELECT count(p.id) "
                            + "AS count "
                            + "FROM post_votes p "
                            + "WHERE p.value = 1",
            nativeQuery = true)
    int countAllLikes();

    @Query(
            value =
                    "SELECT count(p.id) "
                            + "AS count "
                            + "FROM post_votes p "
                            + "WHERE p.value = -1",
            nativeQuery = true)
    int countAllDislikes();

    @Query(
            value =
                    "SELECT * "
                            + "FROM post_votes p"
                            + " WHERE p.post_id = ? "
                            + "AND p.user_id = ?",
            nativeQuery = true)
    PostVote getPostVoteByUserIdAndPostId(int postId, int userId);
}
