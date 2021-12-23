package main.api.mapper;

import main.api.response.PostsMyResponse;
import main.model.Post;
import main.util.LocalTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LocalTimeMapper.class})
public interface PostsMyDTO {
    @Mapping(target = "announce", source = "text")
    @Mapping(target = "timestamp", source = "time")
    @Mapping(target = "commentCount",
            expression = "java(post.getPostComments().size())")
    @Mapping(target = "likeCount",
            expression = "java((int) post.getPostVotes().stream()."
                    + "filter(p -> p.getValue() > 0).count())")
    @Mapping(target = "dislikeCount",
            expression = "java((int) post.getPostVotes().stream()."
                    + "filter(p -> p.getValue() < 0).count())")
    PostsMyResponse.PostDTO toPostDTO(Post post);

    Post toPost(PostsMyResponse.PostDTO postResponseDTO);

    List<PostsMyResponse.PostDTO> toPostDTOs(List<Post> posts);
}
