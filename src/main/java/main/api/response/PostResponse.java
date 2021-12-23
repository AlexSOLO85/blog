package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.Post;
import main.model.TagToPost;
import main.util.LocalTimeMapper;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private int id;
    private long timestamp;
    private boolean active;
    private PostAuthor user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<Comment> comments;
    private List<String> tags;

    public PostResponse(final Post post) {
        id = Math.toIntExact(post.getId());
        timestamp = new LocalTimeMapper().dateToLong(post.getTime());
        active = post.getIsActive();
        user = new PostResponse.PostAuthor(
                Math.toIntExact(post.getUser().getId()),
                post.getUser().getName());
        title = post.getTitle();
        text = post.getText();
        likeCount = (int) post.getPostVotes()
                .stream().filter(l -> l.getValue() == 1).count();
        dislikeCount = (int) post.getPostVotes()
                .stream().filter(l -> l.getValue() == -1).count();
        viewCount = post.getViewCount();
        comments = new ArrayList<>();
        tags = new ArrayList<>();
        post.getPostComments().forEach(postComment -> {
            int commentAuthorId =
                    Math.toIntExact(postComment.getUser().getId());
            String commentAuthorName = postComment.getUser().getName();
            String commentAuthorPhoto = postComment.getUser().getPhoto();
            PostResponse.Comment.CommentAuthor commentAuthor =
                    new PostResponse.Comment.CommentAuthor(
                            commentAuthorId,
                            commentAuthorName,
                            commentAuthorPhoto);
            int commentId = Math.toIntExact(postComment.getId());
            long commentTime = new LocalTimeMapper()
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
        for (TagToPost tagToPost : post.getTagsToPosts()) {
            String tagName = tagToPost.getTag().getName();
            if (!tags.contains(tagName)) {
                tags.add(tagName);
            }
        }
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class PostAuthor {
        private int id;
        private String name;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        private int id;
        private long time;
        private CommentAuthor user;
        private String text;

        @Data
        @RequiredArgsConstructor
        @AllArgsConstructor
        public static class CommentAuthor {
            private int id;
            private String name;
            private String photo;
        }
    }
}
