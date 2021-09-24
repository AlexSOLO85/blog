package main.mapper;

import main.api.response.PostResponse;
import main.model.Post;
import main.util.LocalTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * The interface Post mapper.
 */
@Mapper(componentModel = "spring", uses = LocalTimeMapper.class)
public interface PostMapper {
    /**
     * To post dto post response . post.
     *
     * @param post the post
     * @return the post response . post
     */
    @Mapping(target = "announce", source = "post.text")
    @Mapping(target = "timestamp", source = "post.time")
    @Mapping(target = "post.postVotes", ignore = true)
    PostResponse.PostDTO toPostDTO(Post post);

    /**
     * To post.
     *
     * @param postResponseDTO the post response dto
     * @return the post
     */
    Post toPost(PostResponse.PostDTO postResponseDTO);

    /**
     * To post dt os list.
     *
     * @param posts the posts
     * @return the list
     */
    List<PostResponse.PostDTO> toPostDTOs(List<Post> posts);
}
