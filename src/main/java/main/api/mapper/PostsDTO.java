package main.api.mapper;

import main.api.response.PostsResponse;
import main.model.Post;
import main.utils.LocalTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LocalTimeMapper.class})
public interface PostsDTO {
    @Mapping(target = "announce", source = "post.text")
    @Mapping(target = "timestamp", source = "post.time")
    @Mapping(target = "commentCount",
            expression = "java(post.getPostComments().size())")
    @Mapping(target = "likeCount",
            expression = "java((int) post.getPostVotes().stream()."
                    + "filter(p -> p.getValue() > 0).count())")
    @Mapping(target = "dislikeCount",
            expression = "java((int) post.getPostVotes().stream()."
                    + "filter(p -> p.getValue() < 0).count())")
    PostsResponse.PostDTO toPostDTO(Post post);

    Post toPost(PostsResponse.PostDTO postResponseDTO);

    List<PostsResponse.PostDTO> toPostDTOs(List<Post> posts);
}
