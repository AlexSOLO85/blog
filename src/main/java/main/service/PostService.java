package main.service;

import main.api.response.PostResponse;
import main.mapper.PostMapper;
import main.repository.PostRepository;
import org.springframework.stereotype.Service;

/**
 * The type Post service.
 */
@Service
public class PostService {
    /**
     * The Post repository.
     */
    private final PostRepository postRepository;
    /**
     * The Post mapper.
     */
    private final PostMapper postMapper;

    /**
     * The Post response.
     */
    private final PostResponse postResponse;

    /**
     * Instantiates a new Post service.
     *
     * @param postRepositoryParam the post repository
     * @param postMapperParam     the post mapper param
     * @param postResponseParam   the post response param
     */
    public PostService(final PostRepository postRepositoryParam,
                       final PostMapper postMapperParam,
                       final PostResponse postResponseParam) {
        this.postRepository = postRepositoryParam;
        this.postMapper = postMapperParam;
        this.postResponse = postResponseParam;
    }

    /**
     * Gets post response.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the post response
     */
    public PostResponse getRecentPost(final int offset, final int limit) {
        postResponse.setPostsDTOs(postMapper.toPostDTOs(postRepository
                .recentPost(offset, limit)));
        postResponse.setCount(postResponse.getPostsDTOs().size());
        return postResponse;
    }

    /**
     * Gets best post.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the best post
     */
    public PostResponse getBestPost(final int offset, final int limit) {
        postResponse.setPostsDTOs(postMapper.toPostDTOs(postRepository
                .bestPost(offset, limit)));
        postResponse.setCount(postResponse.getPostsDTOs().size());
        return postResponse;
    }

    /**
     * Gets popular post.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the popular post
     */
    public PostResponse getPopularPost(final int offset, final int limit) {
        postResponse.setPostsDTOs(postMapper.toPostDTOs(postRepository
                .popularPost(offset, limit)));
        postResponse.setCount(postResponse.getPostsDTOs().size());
        return postResponse;
    }

    /**
     * Gets early post.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the early post
     */
    public PostResponse getEarlyPost(final int offset, final int limit) {
        postResponse.setPostsDTOs(postMapper.toPostDTOs(postRepository
                .earlyPost(offset, limit)));
        postResponse.setCount(postResponse.getPostsDTOs().size());
        return postResponse;
    }
}
