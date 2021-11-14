package main.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.CaptchaCode;
import main.model.Post;
import main.model.Tag;
import main.model.TagToPost;
import main.model.User;
import main.model.enumerated.ModerationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Component
public class SaveToEntity {
    public final Post postToEntity(final boolean isActive,
                                   final ModerationStatus moderationStatus,
                                   final User userId,
                                   final LocalDateTime timestamp,
                                   final String title,
                                   final String text,
                                   final int viewCount) {
        Post post = new Post();
        post.setIsActive(isActive);
        post.setModerationStatus(moderationStatus);
        post.setUser(userId);
        post.setTime(timestamp);
        post.setTitle(title);
        post.setText(text);
        post.setViewCount(viewCount);
        return post;
    }
    public final CaptchaCode captchaCodeToEntity(final LocalDateTime now,
                                                 final String token,
                                                 final String secretCode) {
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(now);
        captchaCode.setCode(token);
        captchaCode.setSecretCode(secretCode);
        return captchaCode;
    }
    public final User userToEntity(final boolean isModerator,
                                   final LocalDateTime now,
                                   final String name,
                                   final String email,
                                   final String hashedPassword) {
        User user = new User();
        user.setIsModerator(isModerator);
        user.setRegTime(now);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        return user;
    }
    public final Tag tagToEntity(final String tagName) {
        Tag tag = new Tag();
        tag.setName(tagName);
        return tag;
    }
    public final TagToPost tagToPostToEntity(final Tag tag,
                                             final Post post) {
        TagToPost tagToPost = new TagToPost();
        tagToPost.setTag(tag);
        tagToPost.setPost(post);
        return tagToPost;
    }
}
