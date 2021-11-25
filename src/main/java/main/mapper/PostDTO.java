package main.mapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.api.response.PostResponse;
import main.model.Post;
import main.model.TagToPost;
import main.utils.LocalTimeMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@RequiredArgsConstructor
@Component
public class PostDTO {
    public final PostResponse postResponseDto(final Post post) {
        PostResponse postResponse = new PostResponse();
        ArrayList<PostResponse.Comment> comments = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<>();
        LocalTimeMapper localTimeMapper = new LocalTimeMapper();
        postResponse.setId(Math.toIntExact(post.getId()));
        postResponse.setTimestamp(localTimeMapper.dateToLong(post.getTime()));
        postResponse.setActive(post.getIsActive());
        postResponse.setUser(
                new PostResponse.PostAuthor(Math.toIntExact(
                        post.getUser().getId()), post.getUser().getName()));
        postResponse.setTitle(post.getTitle());
        postResponse.setText(post.getText());
        postResponse.setLikeCount((int) post.getPostVotes()
                .stream().filter(l -> l.getValue() == 1).count());
        postResponse.setDislikeCount((int) post.getPostVotes()
                .stream().filter(l -> l.getValue() == -1).count());
        postResponse.setViewCount(post.getViewCount());
        postResponse.setComments(comments);
        postResponse.setTags(tags);
        post.getPostComments().forEach(postComment -> {
            int commentAuthorId =
                    Math.toIntExact(postComment.getUser().getId());
            String commentAuthorName =
                    postComment.getUser().getName();
            String commentAuthorPhoto =
                    postComment.getUser().getPhoto();
            PostResponse.Comment.CommentAuthor commentAuthor =
                    new PostResponse.Comment.CommentAuthor(
                            commentAuthorId,
                            commentAuthorName,
                            commentAuthorPhoto);
            int commentId = Math.toIntExact(postComment.getId());
            long commentTime = localTimeMapper
                    .dateToLong(postComment.getTime());
            String commentText = postComment.getText();
            PostResponse.Comment comment =
                    new PostResponse.Comment(
                            commentId,
                            commentTime,
                            commentAuthor,
                            commentText);
            comments.add(comment);
        });
        addTag(post, tags);
        return postResponse;
    }

    private void addTag(final Post post, final ArrayList<String> tags) {
        for (TagToPost tagToPost : post.getTagsToPosts()) {
            String tagName = tagToPost.getTag().getName();
            if (!tags.contains(tagName)) {
                tags.add(tagName);
            }
        }
    }
}
