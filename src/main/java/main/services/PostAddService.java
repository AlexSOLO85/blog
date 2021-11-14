package main.services;

import main.api.request.PostAddRequest;
import main.api.response.PostAddBadResponse;
import main.api.response.PostAddSuccessResponse;
import main.utils.SaveToEntity;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.model.enumerated.ModerationStatus;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostAddService {
    @Value("${post.title.min_length}")
    private int postTitleMinLength;
    @Value("${post.text.min_length}")
    private int postTextMinLength;
    private final SaveToEntity saveToEntity;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;
    private final TagToPostService tagToPostService;

    public PostAddService(
            final SaveToEntity saveToEntityParam,
            final PostRepository postRepositoryParam,
            final TagRepository tagRepositoryParam,
            final TagService tagServiceParam,
            final TagToPostService tagToPostServiceParam) {
        this.saveToEntity = saveToEntityParam;
        this.postRepository = postRepositoryParam;
        this.tagRepository = tagRepositoryParam;
        this.tagService = tagServiceParam;
        this.tagToPostService = tagToPostServiceParam;
    }

    public final ResponseEntity<?> addPost(
            final PostAddRequest postAddRequest,
            final User user) {
        PostAddSuccessResponse postAddSuccess
                = new PostAddSuccessResponse();
        LocalDateTime timestamp = postAddRequest.getTime();
        byte active = postAddRequest.getActive();
        String title = postAddRequest.getTitle();
        List<String> tags = postAddRequest.getTags();
        String text = postAddRequest.getText();
        timestamp = timestamp
                == null
                || timestamp.isBefore(LocalDateTime.now())
                ? LocalDateTime.now()
                : timestamp;
        boolean isActive = active == 1;
        ResponseEntity<PostAddBadResponse> response = badResponse(title, text);
        if (response != null) {
            return response;
        }
        Post currentPost = saveToEntity.postToEntity(
                isActive,
                ModerationStatus.NEW,
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
                        saveToEntity.tagToEntity(tag));
                tagToPostService.addTagToPost(
                        saveToEntity
                                .tagToPostToEntity(currentTag, currentPost));
            }
        }
        postAddSuccess.setResult(true);
        return new ResponseEntity<>(postAddSuccess, HttpStatus.OK);
    }

    private ResponseEntity<PostAddBadResponse> badResponse(
            final String title,
            final String text) {
        PostAddBadResponse postAddBad
                = new PostAddBadResponse();
        PostAddBadResponse.ErrorResponse errors
                = new PostAddBadResponse.ErrorResponse();
        if (title.length() < postTitleMinLength) {
            errors.setTitle("Заголовок не установлен");
            postAddBad.setResult(false);
            postAddBad.setErrors(errors);
            return new ResponseEntity<>(postAddBad,
                    HttpStatus.BAD_REQUEST);
        }
        if (text.length() < postTextMinLength) {
            errors.setText("Текст публикации слишком короткий");
            postAddBad.setResult(false);
            postAddBad.setErrors(errors);
            return new ResponseEntity<>(postAddBad,
                    HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
