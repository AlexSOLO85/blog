package main.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import main.model.PostComment;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Component
public class PostCommentResponse {
    private int id;
    public PostCommentResponse(
            final PostComment postComment) {
        id = Math.toIntExact(postComment.getId());
    }
}
