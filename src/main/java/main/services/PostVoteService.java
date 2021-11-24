package main.services;

import lombok.RequiredArgsConstructor;
import main.api.request.PostVoteRequest;
import main.api.response.BooleanResponse;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repository.PostVoteRepository;
import main.utils.SaveToEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class PostVoteService {
    private final PostService postService;
    private final PostVoteRepository postVoteRepository;

    public final ResponseEntity<?> likePost(
            final PostVoteRequest postVoteRequest,
            final User user) {
        return setPostVote(postVoteRequest, user, true);
    }

    public final ResponseEntity<?> dislikePost(
            final PostVoteRequest postVoteRequest,
            final User user) {
        return setPostVote(postVoteRequest, user, false);
    }

    private ResponseEntity<?> setPostVote(
            final PostVoteRequest postVoteRequest,
            final User user,
            final boolean isLike) {
        int postId = postVoteRequest.getPostId();
        if (user == null) {
            return new ResponseEntity<>(new BooleanResponse(false),
                    HttpStatus.BAD_REQUEST);
        }
        Post currentPost = postService.getPostById(postId);
        PostVote beforeLike = postVoteRepository
                .getPostVoteByUserIdAndPostId(postId,
                        Math.toIntExact(user.getId()));
        if (beforeLike == null) {
            postVoteRepository
                    .save(new SaveToEntity().postVoteToEntity(
                            user,
                            currentPost,
                            LocalDateTime.now(ZoneOffset.UTC),
                            (byte) (isLike ? 1 : -1)));
            return new ResponseEntity<>(new BooleanResponse(true),
                    HttpStatus.OK);
        } else if (beforeLike.getValue() == (isLike ? 1 : -1)) {
            return new ResponseEntity<>(new BooleanResponse(false),
                    HttpStatus.OK);
        } else if (beforeLike.getValue() == (isLike ? -1 : 1)) {
            currentPost.getPostVotes().remove(beforeLike);
            postVoteRepository.delete(beforeLike);
            postVoteRepository
                    .save(new SaveToEntity().postVoteToEntity(
                            user,
                            currentPost,
                            LocalDateTime.now(ZoneOffset.UTC),
                            (byte) (isLike ? 1 : -1)));
            return new ResponseEntity<>(new BooleanResponse(true),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new BooleanResponse(false),
                    HttpStatus.OK);
        }
    }
}
