package main.controller;

import main.api.response.PostResponse;
import main.mapper.PostMapper;
import main.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Api post controller.
 */
@RestController
@RequestMapping("/api")
public class ApiPostController {
    /**
     * The Post service.
     */
    private final PostService postService;
    /**
     * The Post mapper.
     */
    private final PostMapper postMapper;

    /**
     * Instantiates a new Api post controller.
     *
     * @param postServices the post service
     * @param postMappers  the post mapper
     */
    public ApiPostController(final PostService postServices,
                             final PostMapper postMappers) {
        this.postService = postServices;
        this.postMapper = postMappers;
    }

    /**
     * Find all response entity.
     *
     * @param offset the offset
     * @param limit  the limit
     * @param mode   the mode
     * @return the response entity
     */
    @GetMapping("/post")
    public ResponseEntity<List<PostResponse.PostDTO>> findAll(
            final @RequestParam(defaultValue = "0") int offset,
            final @RequestParam(defaultValue = "10") int limit,
            final @RequestParam(defaultValue = "recent") String mode) {
        return ResponseEntity.ok(
                postMapper.toPostDTOs(postService.getPostResponse()));
    }
}
