package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import main.util.HtmlParser;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostsMyResponse {
    private static final int STRING_LENGTH = 150;
    private int count;
    @JsonProperty("posts")
    private List<PostDTO> postsDTOs;

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class PostDTO {
        private int id;
        private int timestamp;
        private String title;
        @Setter(value = AccessLevel.NONE)
        private String announce;
        private int likeCount;
        private int dislikeCount;
        private int commentCount;
        private int viewCount;
        private UserDTO user;

        public final void setAnnounce(final String announceLength) {
            String temp = HtmlParser.html2text(announceLength);
            assert temp != null;
            announce = (temp.length() > STRING_LENGTH)
                    ? temp.substring(0, STRING_LENGTH - 1)
                    .concat("...") : temp;
        }
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class UserDTO {
        private int id;
        private String name;
    }
}
