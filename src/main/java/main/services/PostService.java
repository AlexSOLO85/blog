package main.services;

import lombok.RequiredArgsConstructor;
import main.api.request.PostAddRequest;
import main.api.request.PostCommentRequest;
import main.api.request.PostModerateRequest;
import main.api.response.BooleanResponse;
import main.api.response.PostCommentResponse;
import main.api.response.PostResponse;
import main.api.response.badresponse.PostAddBadResponse;
import main.api.response.badresponse.PostCommentBadResponse;
import main.mapper.PostDTO;
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
import main.utils.SaveToEntity;
import org.springframework.beans.factory.annotation.Value;
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
    private final PostDTO postDTO;

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
            return new ResponseEntity<>(postDTO.postResponseDto(post),
                    HttpStatus.OK);
        }
    }

    private PostResponse getPostNotLoginUser(final Post post) {
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return postDTO.postResponseDto(post);
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
        ResponseEntity<PostAddBadResponse> response = badResponse(title, text);
        if (response != null) {
            return response;
        }
        Post currentPost = new SaveToEntity().postToEntity(
                isActive,
                isCurrentSettingOn() ? ModerationStatus.NEW
                        : ModerationStatus.ACCEPTED,
                user,
                timestamp,
                title,
                text,
                0);
        postRepository.save(currentPost);
        for (String tag : tags) {
            if (!tag.isBlank()) {
                Tag currentTag = tagRepository
                        .getTagByTagName(tag) != null
                        ? tagRepository.getTagByTagName(tag)
                        : tagService.addTag(
                        new SaveToEntity().tagToEntity(tag));
                tagToPostService.addTagToPost(
                        new SaveToEntity()
                                .tagToPostToEntity(currentTag, currentPost));
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
        ResponseEntity<PostAddBadResponse> response = badResponse(title, text);
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
                        .getTagByTagName(tag) != null
                        ? tagRepository.getTagByTagName(tag)
                        : tagService.addTag(
                        new SaveToEntity().tagToEntity(tag));
                TagToPost tagToPost = new SaveToEntity()
                        .tagToPostToEntity(currentTag, post);
                tagToPostService.addTagToPost(tagToPost);
            }
        }
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }

    public final ResponseEntity<?> addComment(
            final PostCommentRequest postCommentRequest,
            final User user) {
        PostCommentBadResponse postCommentBadResponse
                = new PostCommentBadResponse();
        PostCommentBadResponse.Errors error
                = new PostCommentBadResponse.Errors();
        Integer parentId = postCommentRequest.getParentId();
        Integer postId = postCommentRequest.getPostId();
        String text = postCommentRequest.getText();
        if (!isTextValid(text)) {
            error.setText("Текст комментария не задан или слишком короткий");
            postCommentBadResponse.setResult(false);
            postCommentBadResponse.setErrors(error);
            return new ResponseEntity<>(postCommentBadResponse,
                    HttpStatus.BAD_REQUEST);
        }
        PostComment parentComment = null;
        Post parentPost = null;
        if (parentId != null) {
            parentComment = getPostCommentById(parentId);
        }
        if (postId != null) {
            parentPost = getPostById(postId);
        }
        PostComment newComment = postCommentRepository.save(
                new SaveToEntity().postCommentToEntity(
                        parentComment,
                        user,
                        parentPost,
                        LocalDateTime.now(ZoneOffset.UTC),
                        text));
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
                        .equalsIgnoreCase(SettingsService.POST_PREMODERATION))
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

    private ResponseEntity<PostAddBadResponse> badResponse(
            final String title,
            final String text) {
        PostAddBadResponse postAddBad
                = new PostAddBadResponse();
        PostAddBadResponse.Errors errors
                = new PostAddBadResponse.Errors();
        if (title.length() < postTitleMinLength) {
            errors.setTitle("Заголовок не установлен");
            postAddBad.setResult(false);
            postAddBad.setErrors(errors);
            return new ResponseEntity<>(postAddBad, HttpStatus.BAD_REQUEST);
        }
        if (text.length() < postTextMinLength) {
            errors.setText("Текст публикации слишком короткий");
            postAddBad.setResult(false);
            postAddBad.setErrors(errors);
            return new ResponseEntity<>(postAddBad, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
