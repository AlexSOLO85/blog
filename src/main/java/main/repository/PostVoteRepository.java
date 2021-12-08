package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostVoteRepository
        extends PagingAndSortingRepository<PostVote, Long> {
    @Query("select count(p.id) from PostVote p "
            + "where p.value = 1")
    int countAllLikes();

    @Query("select count(p.id) from PostVote p "
            + "where p.value = -1")
    int countAllDislikes();

    PostVote getPostVoteByPostIdAndUserId(long postId, long userId);
}
