package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import main.util.HtmlParser;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Post response.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class PostResponse {
    /**
     * The constant STRING_LENGTH.
     */
    private static final int STRING_LENGTH = 150;
    /**
     * The Count.
     */
    private int count;
    /**
     * The Posts.
     */
    @JsonProperty("posts")
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
        private UserDTO user;
        /**
         * The Title.
         */
        private String title;
        /**
         * The Announce.
         */
        @Setter(value = AccessLevel.NONE)
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

        /**
         * Sets announce.
         *
         * @param announceLength the announceLength
         */
        public void setAnnounce(final String announceLength) {
            String temp = HtmlParser.html2text(announceLength);
            assert temp != null;
            announce = (temp.length() > STRING_LENGTH)
                    ? temp.substring(0, STRING_LENGTH - 1)
                    .concat("...") : temp;
        }
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
