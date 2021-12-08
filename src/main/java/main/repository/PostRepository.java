package main.repository;

import main.model.Post;
import main.model.enumerated.ModerationStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    @Query("select count(p.id) "
            + "from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time")
    int countAllPosts();

    @Query("select count(p.id) "
            + "from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'NEW'")
    int countModeratePosts();

    @Query("select p from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "order by p.time desc")
    List<Post> recentPosts(Pageable pageable);

    @Query("select p, sum(pv.value) as sum_votes from Post p "
            + "left join p.postVotes pv "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "group by p.id "
            + "order by sum_votes desc")
    List<Post> bestPosts(Pageable pageable);

    @Query("select p, count(pc.id) as sum_comments from Post p "
            + "left join p.postComments pc "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "group by p.id "
            + "order by sum_comments desc")
    List<Post> popularPosts(Pageable pageable);

    @Query("select p from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "order by p.time")
    List<Post> earlyPosts(Pageable pageable);

    @Query("select distinct p from Post p "
            + "where p.text like %:query% "
            + "or p.title like %:query% "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "order by p.time")
    List<Post> searchPosts(Pageable pageable, @Param("query") String query);

    @Query("select distinct count(p.id) from Post p "
            + "where p.text like %:query% "
            + "or p.title like %:query% "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time")
    int countSearchedPosts(@Param("query") String query);

    @Query("select p from Post p "
            + "where substring(p.time, 1, 4) = :year "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time")
    List<Post> getPostsByYear(@Param("year") String year);

    @Query("select distinct substring(p.time, 1, 4) from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time")
    List<Integer> getYearsWithAnyPosts();

    @Query("select p from Post p "
            + "where function('DATEDIFF', p.time, :date) = 0 "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "order by p.time")
    List<Post> getPostsByDate(@Param("date") String date, Pageable pageable);

    @Query("select count(p.id) from Post p "
            + "where function('DATEDIFF', p.time, :dateString) = 0 "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time")
    int countPostsByDate(@Param("dateString") String dateString);

    @Query("select distinct p, t2p from Post p "
            + "inner join p.tagsToPosts t2p "
            + "where t2p.tag.name like %:tag% "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time "
            + "order by p.time")
    List<Post> getPostsByTag(@Param("tag") String tag, Pageable pageable);

    @Query("select distinct count(p.id) from Post p "
            + "inner join p.tagsToPosts t2p "
            + "where t2p.tag.name like %:tag% "
            + "and p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "and p.time < current_time")
    int countPostsByTag(@Param("tag") String tag);

    @Query("select p from Post p "
            + "where p.user.id = :userId "
            + "and p.isActive = true "
            + "and p.moderationStatus = :status "
            + "order by p.time")
    List<Post> getMyActivePosts(Pageable pageable,
                                @Param("status") ModerationStatus status,
                                @Param("userId") long userId);

    @Query("select count(p.id) from Post p "
            + "where p.user.id = :userId "
            + "and p.isActive = true "
            + "and p.moderationStatus = :status")
    int countMyActivePosts(@Param("status") ModerationStatus status,
                           @Param("userId") long userId);

    @Query("select p from Post p "
            + "where p.user.id = :userId "
            + "and p.isActive = false "
            + "order by p.time")
    List<Post> getMyNotActivePosts(Pageable pageable,
                                   @Param("userId") long userId);

    @Query("select count(p.id) from Post p "
            + "where p.user.id = :userId "
            + "and p.isActive = false")
    int countMyNotActivePosts(@Param("userId") long userId);

    @Query("select p from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'NEW' "
            + "order by p.time")
    List<Post> getPostsForModeration(Pageable pageable);

    @Query("select count(p.id) from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'NEW'")
    int countPostsForModeration();

    @Query("select p from Post p "
            + "where p.moderatorId = :moderatorId "
            + "and p.isActive = true "
            + "and p.moderationStatus = :status "
            + "order by p.time")
    List<Post> getPostsModeratedByMe(Pageable pageable,
                                     @Param("status") ModerationStatus status,
                                     @Param("moderatorId") int moderatorId);

    @Query("select count(p.id) from Post p "
            + "where p.moderatorId = :moderatorId "
            + "and p.isActive = true "
            + "and p.moderationStatus = :moderationStatus")
    int countPostsModeratedByMe(
            @Param("moderationStatus") String moderationStatus,
            @Param("moderatorId") int moderatorId);

    @Query("select count(p) from Post p")
    int countAllPostsAtDatabase();

    @Query("select sum(p.viewCount) from Post p")
    int countAllViews();

    @Query("select p.time from Post p "
            + "where p.isActive = true "
            + "and p.moderationStatus = 'ACCEPTED' "
            + "order by p.time")
    LocalDateTime getFirstPublicationDate(PageRequest pageRequest);
}
