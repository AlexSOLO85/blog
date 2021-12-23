package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.PostComment;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostCommentResponse {
    private int id;
    public PostCommentResponse(
            final PostComment postComment) {
        id = Math.toIntExact(postComment.getId());
    }
}
