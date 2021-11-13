package main.services;

import main.api.response.PostsMyResponse;
import main.api.response.PostsResponse;
import main.mapper.PostsDTO;
import main.mapper.PostsMyDTO;
import main.model.enumerated.ModerationStatus;
import main.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostsService {
    private final PostRepository postRepository;
    private final PostsDTO postsDTO;
    private final PostsMyDTO postsMyDTO;
    private final PostsResponse postsResponse;
    private final PostsMyResponse postsMyResponse;

    public PostsService(final PostRepository postRepositoryParam,
                        final PostsDTO postsDTOParam,
                        final PostsMyDTO postsMyDTOParam,
                        final PostsResponse postsResponseParam,
                        final PostsMyResponse postsMyResponseParam) {
        this.postRepository = postRepositoryParam;
        this.postsDTO = postsDTOParam;
        this.postsMyDTO = postsMyDTOParam;
        this.postsResponse = postsResponseParam;
        this.postsMyResponse = postsMyResponseParam;
    }

    public final ResponseEntity<PostsResponse> getRecentPosts(
            final int offset,
            final int limit) {
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .recentPosts(offset, limit)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getBestPosts(
            final int offset,
            final int limit) {
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .bestPosts(offset, limit)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getPopularPosts(
            final int offset,
            final int limit) {
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .popularPosts(offset, limit)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getEarlyPosts(
            final int offset,
            final int limit) {
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .earlyPosts(offset, limit)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getQueryPosts(
            final int offset,
            final int limit,
            final String query) {
        postsResponse.setCount(postRepository.countSearchedPosts(query));
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .searchPosts(offset, limit, query)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getPostByDate(
            final int offset,
            final int limit,
            final String date) {
        postsResponse.setCount(postRepository.countPostsByDate(date));
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .getPostsByDate(date, limit, offset)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getPostsByTag(
            final int offset,
            final int limit,
            final String tag) {
        postsResponse.setCount(postRepository.countPostsByTag(tag));
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .getPostsByTag(tag, limit, offset)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsMyResponse> getMyPosts(
            final int offset,
            final int limit,
            final String status,
            final int userId) {
        switch (status) {
            case "inactive":
                postsMyResponse.setCount(
                        postRepository.countMyNotActivePosts(
                                userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyNotActivePosts(
                                        offset,
                                        limit,
                                        userId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "pending":
                postsMyResponse.setCount(
                        postRepository.countMyActivePosts(
                                ModerationStatus.NEW.toString(), userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyActivePosts(
                                        offset,
                                        limit,
                                        ModerationStatus.NEW.toString(),
                                        userId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "declined":
                postsMyResponse.setCount(
                        postRepository.countMyActivePosts(
                                ModerationStatus.DECLINE.toString(), userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyActivePosts(
                                        offset,
                                        limit,
                                        ModerationStatus.DECLINE.toString(),
                                        userId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "published":
                postsMyResponse.setCount(
                        postRepository.countMyActivePosts(
                                ModerationStatus.ACCEPTED.toString(), userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyActivePosts(
                                        offset,
                                        limit,
                                        ModerationStatus.ACCEPTED.toString(),
                                        userId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    public final ResponseEntity<PostsMyResponse> getModeratePosts(
            final int offset,
            final int limit,
            final String status,
            final int moderateUserId) {
        switch (status.toLowerCase()) {
            case "new":
                postsMyResponse.setCount(
                        postRepository.countPostsForModeration());
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getPostsForModeration(
                                        offset,
                                        limit)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "declined":
                postsMyResponse.setCount(
                        postRepository.countPostsModeratedByMe(
                                ModerationStatus.DECLINE.toString(),
                                moderateUserId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getPostsModeratedByMe(
                                        offset,
                                        limit,
                                        ModerationStatus.DECLINE.toString(),
                                        moderateUserId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "accepted":
                postsMyResponse.setCount(
                        postRepository.countPostsModeratedByMe(
                                ModerationStatus.ACCEPTED.toString(),
                                moderateUserId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getPostsModeratedByMe(
                                        offset,
                                        limit,
                                        ModerationStatus.ACCEPTED.toString(),
                                        moderateUserId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }
}
