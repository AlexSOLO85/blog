package main.service;

import lombok.RequiredArgsConstructor;
import main.api.mapper.PostsDTO;
import main.api.mapper.PostsMyDTO;
import main.api.request.PostAddRequest;
import main.api.request.PostCommentRequest;
import main.api.request.PostModerateRequest;
import main.api.response.BooleanResponse;
import main.api.response.PostCommentResponse;
import main.api.response.PostResponse;
import main.api.response.BadResponse;
import main.api.response.PostsMyResponse;
import main.api.response.PostsResponse;
import main.model.GlobalSettings;
import main.model.Post;
import main.model.PostComment;
import main.model.Tag;
import main.model.TagToPost;
import main.model.User;
import main.model.enumerated.ModerationStatus;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    @Value("${post.title.min_length}")
    private int postTitleMinLength;
    @Value("${post.text.min_length}")
    private int postTextMinLength;
    @Value("${post_comment.min_length}")
    private int minCommentLength;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostCommentRepository postCommentRepository;
    private final TagService tagService;
    private final TagToPostService tagToPostService;
    private final UserService userService;
    private final SettingsService settingsService;
    private final PostsDTO postsDTO;
    private final PostsMyDTO postsMyDTO;

    public final Post getPostById(final int id) {
        Optional<Post> optionalPost = postRepository.findById((long) id);
        return optionalPost.orElseThrow(NullPointerException::new);
    }

    public final ResponseEntity<PostResponse> getPost(final int id) {
        Post post = getPostById(id);
        Authentication loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUser(username);
        if (user == null
                || (Objects.requireNonNull(post).getUser()
                != user && !user.getIsModerator())) {
            assert post != null;
            return new ResponseEntity<>(getPostNotLoginUser(post),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new PostResponse(post),
                    HttpStatus.OK);
        }
    }

    public final ResponseEntity<?> addPost(
            final PostAddRequest postAddRequest,
            final User user) {
        LocalDateTime timestamp = postAddRequest.getTime();
        byte active = postAddRequest.getActive();
        String title = postAddRequest.getTitle();
        List<String> tags = postAddRequest.getTags();
        String text = postAddRequest.getText();
        timestamp = timestamp
                == null
                || timestamp.isBefore(LocalDateTime.now(ZoneOffset.UTC))
                ? LocalDateTime.now(ZoneOffset.UTC)
                : timestamp;
        boolean isActive = active == 1;
        ResponseEntity<BadResponse> response = badResponse(title, text);
        if (response != null) {
            return response;
        }
        Post currentPost = new Post(isActive,
                isCurrentSettingOn() ? ModerationStatus.NEW
                        : ModerationStatus.ACCEPTED,
                user, timestamp, title, text, 0);
        postRepository.save(currentPost);
        for (String tag : tags) {
            if (!tag.isBlank()) {
                Tag currentTag = tagRepository
                        .getTagByName(tag) != null
                        ? tagRepository.getTagByName(tag)
                        : tagService.addTag(new Tag(tag));
                tagToPostService.addTagToPost(new TagToPost(currentTag,
                        currentPost));
            }
        }
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }

    public final ResponseEntity<?> editPost(
            final int id,
            final PostAddRequest postAddRequest,
            final User user) {
        Post post = getPostById(id);
        LocalDateTime timestamp = getLocalDateTime(postAddRequest);
        byte active = getActive(postAddRequest);
        String title = postAddRequest.getTitle();
        List<String> tags = postAddRequest.getTags();
        String text = postAddRequest.getText();
        timestamp = timestamp
                == null
                || timestamp.isBefore(LocalDateTime.now(ZoneOffset.UTC))
                ? LocalDateTime.now(ZoneOffset.UTC)
                : timestamp;
        ResponseEntity<BadResponse> response = badResponse(title, text);
        if (response != null) {
            return response;
        }
        boolean isActive = active == 1;
        post.setIsActive(isActive);
        post.setTime(timestamp);
        post.setTitle(title);
        post.setText(text);
        if (Boolean.TRUE.equals(!user.getIsModerator())
                && post.getUser() == user) {
            post.setModerationStatus(ModerationStatus.NEW);
        } else {
            post.setModerationStatus(ModerationStatus.ACCEPTED);
        }
        postRepository.save(post);
        List<TagToPost> tagsToPosts = post.getTagsToPosts();
        int i = 0;
        while (i < tagsToPosts.size()) {
            TagToPost tag2post = tagsToPosts.get(i);
            post.getTagsToPosts().remove(tag2post);
            tagToPostService.deleteTagToPost(tag2post);
            i++;
        }
        for (String tag : tags) {
            if (!tag.isBlank()) {
                Tag currentTag = tagRepository
                        .getTagByName(tag) != null
                        ? tagRepository.getTagByName(tag)
                        : tagService.addTag(new Tag(tag));
                TagToPost tagToPost = new TagToPost(currentTag, post);
                tagToPostService.addTagToPost(tagToPost);
            }
        }
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }

    public final ResponseEntity<?> addComment(
            final PostCommentRequest postCommentRequest,
            final User user) {
        BadResponse badResponse = new BadResponse();
        BadResponse.Errors error = new BadResponse.Errors();
        Integer parentId = postCommentRequest.getParentId();
        Integer postId = postCommentRequest.getPostId();
        String text = postCommentRequest.getText();
        if (!isTextValid(text)) {
            error.setText("Текст комментария не задан или слишком короткий");
            badResponse.setResult(false);
            badResponse.setErrors(error);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        PostComment parentComment = null;
        Post parentPost = null;
        if (parentId != null) {
            parentComment = getPostCommentById(parentId);
        }
        if (postId != null) {
            parentPost = getPostById(postId);
        }
        assert parentComment != null;
        assert parentPost != null;
        PostComment newComment = postCommentRepository.save(
                new PostComment(parentComment, parentPost, user,
                        LocalDateTime.now(ZoneOffset.UTC), text));
        return new ResponseEntity<>(
                new PostCommentResponse(
                        Math.toIntExact(newComment.getId())), HttpStatus.OK);
    }

    public final ResponseEntity<?> moderatePost(
            final PostModerateRequest postModerateRequest,
            final User user) {
        int postId = postModerateRequest.getPostId();
        String decision = postModerateRequest.getDecision();
        decision = decision.toUpperCase().trim();
        if (Boolean.FALSE.equals(user.getIsModerator())) {
            return new ResponseEntity<>(
                    new BooleanResponse(false), HttpStatus.BAD_REQUEST);
        }
        Post post = getPostById(postId);
        post.setModeratorId(user.getId());
        post.setModerationStatus(decision.equals("DECLINE")
                ? ModerationStatus.DECLINED
                : ModerationStatus.ACCEPTED);
        postRepository.save(post);
        return new ResponseEntity<>(
                new BooleanResponse(true), HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getRecentPosts(
            final int offset,
            final int limit) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .recentPosts(PageRequest.of(offset / limit, limit))));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getBestPosts(
            final int offset,
            final int limit) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .bestPosts(PageRequest.of(offset / limit, limit))));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getPopularPosts(
            final int offset,
            final int limit) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .popularPosts(PageRequest.of(offset / limit, limit))));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getEarlyPosts(
            final int offset,
            final int limit) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(postRepository.countAllPosts());
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .earlyPosts(PageRequest.of(offset / limit, limit))));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getQueryPosts(
            final int offset,
            final int limit,
            final String query) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(postRepository.countSearchedPosts(query));
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .searchPosts(PageRequest.of(offset / limit, limit), query)));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getPostByDate(
            final int offset,
            final int limit,
            final String date) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(postRepository.countPostsByDate(date));
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .getPostsByDate(date, PageRequest.of(offset / limit, limit))));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsResponse> getPostsByTag(
            final int offset,
            final int limit,
            final String tag) {
        PostsResponse postsResponse = new PostsResponse();
        postsResponse.setCount(postRepository.countPostsByTag(tag));
        postsResponse.setPostsDTOs(postsDTO.toPostDTOs(postRepository
                .getPostsByTag(tag, PageRequest.of(offset / limit, limit))));
        return new ResponseEntity<>(postsResponse, HttpStatus.OK);
    }

    public final ResponseEntity<PostsMyResponse> getMyPosts(
            final int offset,
            final int limit,
            final String status,
            final long userId) {
        PostsMyResponse postsMyResponse = new PostsMyResponse();
        switch (status) {
            case "inactive":
                postsMyResponse.setCount(
                        postRepository.countMyNotActivePosts(
                                userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyNotActivePosts(PageRequest
                                        .of(offset / limit, limit), userId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "pending":
                postsMyResponse.setCount(
                        postRepository.countMyActivePosts(
                                ModerationStatus.NEW, userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyActivePosts(PageRequest
                                                .of(offset / limit, limit),
                                        ModerationStatus.NEW,
                                        userId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "declined":
                postsMyResponse.setCount(
                        postRepository.countMyActivePosts(
                                ModerationStatus.DECLINED, userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyActivePosts(PageRequest
                                                .of(offset / limit, limit),
                                        ModerationStatus.DECLINED,
                                        userId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "published":
                postsMyResponse.setCount(
                        postRepository.countMyActivePosts(
                                ModerationStatus.ACCEPTED, userId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getMyActivePosts(PageRequest
                                                .of(offset / limit, limit),
                                        ModerationStatus.ACCEPTED,
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
        PostsMyResponse postsMyResponse = new PostsMyResponse();
        switch (status.toLowerCase()) {
            case "new":
                postsMyResponse.setCount(
                        postRepository.countPostsForModeration());
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getPostsForModeration(PageRequest
                                        .of(offset / limit, limit))));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "declined":
                postsMyResponse.setCount(
                        postRepository.countPostsModeratedByMe(
                                ModerationStatus.DECLINED.toString(),
                                moderateUserId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getPostsModeratedByMe(PageRequest
                                                .of(offset / limit, limit),
                                        ModerationStatus.DECLINED,
                                        moderateUserId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            case "accepted":
                postsMyResponse.setCount(
                        postRepository.countPostsModeratedByMe(
                                ModerationStatus.ACCEPTED.toString(),
                                moderateUserId));
                postsMyResponse.setPostsDTOs(
                        postsMyDTO.toPostDTOs(postRepository
                                .getPostsModeratedByMe(PageRequest
                                                .of(offset / limit, limit),
                                        ModerationStatus.ACCEPTED,
                                        moderateUserId)));
                return new ResponseEntity<>(postsMyResponse, HttpStatus.OK);
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    private PostResponse getPostNotLoginUser(final Post post) {
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return new PostResponse(post);
    }

    private PostComment getPostCommentById(final int id) {
        Optional<PostComment> optionalComment
                = postCommentRepository.findById((long) id);
        return optionalComment.orElse(null);
    }

    private boolean isTextValid(final String text) {
        if (text == null || text.equals("")) {
            return false;
        }
        return text.length() >= minCommentLength;
    }

    private boolean isCurrentSettingOn() {
        Optional<GlobalSettings> settingsOptional = settingsService
                .getAllGlobalSettingsSet()
                .stream().filter(g -> g.getCode()
                        .equalsIgnoreCase("POST_PREMODERATION"))
                .findFirst();
        return settingsOptional.isPresent()
                && settingsOptional.get().getValue().equalsIgnoreCase("YES");
    }

    private byte getActive(
            final PostAddRequest postAddRequest) {
        return postAddRequest.getActive();
    }

    private LocalDateTime getLocalDateTime(
            final PostAddRequest postAddRequest) {
        return postAddRequest.getTime();
    }

    private ResponseEntity<BadResponse> badResponse(
            final String title,
            final String text) {
        BadResponse badResponse = new BadResponse();
        BadResponse.Errors error = new BadResponse.Errors();
        if (title.length() < postTitleMinLength) {
            error.setTitle("Заголовок не установлен");
            badResponse.setResult(false);
            badResponse.setErrors(error);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        if (text.length() < postTextMinLength) {
            error.setText("Текст публикации слишком короткий");
            badResponse.setResult(false);
            badResponse.setErrors(error);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
