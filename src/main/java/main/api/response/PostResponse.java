package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * The type Post response.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostResponse {
    /**
     * The Count.
     */
    private int count;
    /**
     * The Posts.
     */
    private List<PostDTO> postsDTOs;

    /**
     * The type Post.
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PostDTO {
        /**
         * The Id.
         */
        private int id;
        /**
         * The Timestamp.
         */
        private int timestamp;
        /**
         * The User.
         */
        private UserDTO userDTO;
        /**
         * The Title.
         */
        private String title;
        /**
         * The Announce.
         */
        private String announce;
        /**
         * The Like count.
         */
        private int likeCount;
        /**
         * The Dislike count.
         */
        private int dislikeCount;
        /**
         * The Comment count.
         */
        private int commentCount;
        /**
         * The View count.
         */
        private int viewCount;
    }

    /**
     * The type User.
     */
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class UserDTO {
        /**
         * The Id.
         */
        private int id;
        /**
         * The Name.
         */
        private String name;
    }
}
