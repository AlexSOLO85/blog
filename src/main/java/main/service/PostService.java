package main.service;

import main.model.Post;
import main.repository.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * Instantiates a new Post service.
     *
     * @param postRepositoryParam the post repository
     */
    public PostService(final PostRepository postRepositoryParam) {
        this.postRepository = postRepositoryParam;
    }

    /**
     * Gets post response.
     *
     * @return the post response
     */
    public List<Post> getPostResponse() {
        return postRepository.getAllPost(Pageable.unpaged());
    }
}
