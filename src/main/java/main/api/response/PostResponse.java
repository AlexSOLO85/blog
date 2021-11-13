package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
