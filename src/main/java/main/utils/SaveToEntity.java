package main.utils;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.CaptchaCode;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVote;
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
                                   final LocalDateTime time,
                                   final String title,
                                   final String text,
                                   final int viewCount) {
        Post post = new Post();
        post.setIsActive(isActive);
        post.setModerationStatus(moderationStatus);
        post.setUser(userId);
        post.setTime(time);
        post.setTitle(title);
        post.setText(text);
        post.setViewCount(viewCount);
        return post;
    }

    public final CaptchaCode captchaCodeToEntity(final LocalDateTime time,
                                                 final String token,
                                                 final String secretCode) {
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setTime(time);
        captchaCode.setCode(token);
        captchaCode.setSecretCode(secretCode);
        return captchaCode;
    }

    public final User userToEntity(final boolean isModerator,
                                   final LocalDateTime time,
                                   final String name,
                                   final String email,
                                   final String hashedPassword,
                                   final String defaultPhoto) {
        User user = new User();
        user.setIsModerator(isModerator);
        user.setRegTime(time);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setPhoto(defaultPhoto);
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

    public final PostComment postCommentToEntity(
            final PostComment parentComment,
            final User user,
            final Post parentPost,
            final LocalDateTime time,
            final String text) {
        PostComment postComment = new PostComment();
        postComment.setParentId(parentComment);
        postComment.setUser(user);
        postComment.setPost(parentPost);
        postComment.setTime(time);
        postComment.setText(text);
        return postComment;
    }

    public final PostVote postVoteToEntity(
            final User user,
            final Post currentPost,
            final LocalDateTime time,
            final byte value) {
        PostVote postVote = new PostVote();
        postVote.setUser(user);
        postVote.setPost(currentPost);
        postVote.setTime(time);
        postVote.setValue(value);
        return postVote;
    }
}
