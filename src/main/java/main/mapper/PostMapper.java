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
@Mapper(componentModel = "spring", uses = {LocalTimeMapper.class})
public abstract class PostMapper {
    /**
     * To post dto post response . post.
     *
     * @param post the post
     * @return the post response . post
     */
    @Mapping(target = "announce", source = "post.text")
    @Mapping(target = "timestamp", source = "post.time")
    @Mapping(target = "commentCount",
            expression = "java(post.getPostComments().size())")
    @Mapping(target = "likeCount",
            expression = "java((int) post.getPostVotes().stream()"
                    + ".filter(p -> p.getValue() > 0).count())")
    @Mapping(target = "dislikeCount",
            expression = "java((int) post.getPostVotes().stream()"
                    + ".filter(p -> p.getValue() < 0).count())")
    public abstract PostResponse.PostDTO toPostDTO(Post post);

    /**
     * To post.
     *
     * @param postResponseDTO the post response dto
     * @return the post
     */
    public abstract Post toPost(PostResponse.PostDTO postResponseDTO);

    /**
     * To post dt os list.
     *
     * @param posts the posts
     * @return the list
     */
    public abstract List<PostResponse.PostDTO> toPostDTOs(List<Post> posts);
}
