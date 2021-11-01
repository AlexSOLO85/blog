package main.controller;

import main.api.response.BadResponse;
import main.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * Instantiates a new Api post controller.
     *
     * @param postServices the post service
     */
    public ApiPostController(final PostService postServices) {
        this.postService = postServices;
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
    private Object findRecentPost(
            final @RequestParam(defaultValue = "0") int offset,
            final @RequestParam(defaultValue = "10") int limit,
            final @RequestParam(defaultValue = "recent") String mode) {
        boolean isModeValid = mode.equals("recent") || mode.equals("popular")
                || mode.equals("best") || mode.equals("early");
        if (offset < 0 || limit < 1 || !isModeValid) {
            return new ResponseEntity<>(new BadResponse(
                    offset < 0 ? "Передан отрицательный параметр сдвига" : "",
                    limit < 1 ? "Ограничение количества "
                            + "отображаемых постов менее 1" : "",
                    !isModeValid ? "Режим отображения не распознан" : ""),
                    HttpStatus.BAD_REQUEST);
        }
        switch (mode) {
            case "recent":
                return ResponseEntity.ok((postService
                        .getRecentPost(offset, limit)));
            case "popular":
                return ResponseEntity.ok(postService
                        .getPopularPost(offset, limit));
            case "best":
                return ResponseEntity.ok(postService
                        .getBestPost(offset, limit));
            case "early":
                return ResponseEntity.ok(postService
                        .getEarlyPost(offset, limit));
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
    }
}
