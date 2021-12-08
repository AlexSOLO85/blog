package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.PostAddRequest;
import main.api.request.PostVoteRequest;
import main.api.response.PostResponse;
import main.api.response.PostsMyResponse;
import main.api.response.PostsResponse;
import main.model.User;
import main.services.PostService;
import main.services.PostVoteService;
import main.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiPostController {
    private final PostService postService;
    private final UserService userService;
    private final PostVoteService postVoteService;

    @GetMapping(value = "/post", params = {"offset", "limit", "mode"})
    public final ResponseEntity<PostsResponse> getPostsWithParam(
            final @RequestParam(value = "offset") int offset,
            final @RequestParam(value = "limit") int limit,
            final @RequestParam(value = "mode") String mode) {
        switch (mode) {
            case "recent":
                return postService.getRecentPosts(offset, limit);
            case "popular":
                return postService.getPopularPosts(offset, limit);
            case "best":
                return postService.getBestPosts(offset, limit);
            case "early":
                return postService.getEarlyPosts(offset, limit);
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }
    }

    @GetMapping(value = "post/search", params = {"offset", "limit", "query"})
    public final ResponseEntity<PostsResponse> getPostsWithQuery(
            final @RequestParam(value = "offset") int offset,
            final @RequestParam(value = "limit") int limit,
            final @RequestParam(value = "query") String query) {
        if (query.isBlank() || query.isEmpty() || query.matches("\\s+")) {
            return postService.getRecentPosts(offset, limit);
        } else {
            return postService.getQueryPosts(offset, limit, query);
        }
    }

    @GetMapping(value = "post/byDate", params = {"offset", "limit", "date"})
    public final ResponseEntity<PostsResponse> getPostsByDate(
            final @RequestParam(value = "offset") int offset,
            final @RequestParam(value = "limit") int limit,
            final @RequestParam(value = "date") String date) {
        return postService.getPostByDate(offset, limit, date);
    }

    @GetMapping(value = "/post/byTag", params = {"offset", "limit", "tag"})
    public final ResponseEntity<PostsResponse> getPostsByTag(
            final @RequestParam(value = "offset") int offset,
            final @RequestParam(value = "limit") int limit,
            final @RequestParam(value = "tag") String tag) {
        return postService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping(value = "/post/{id}")
    public final ResponseEntity<PostResponse> getPostById(
            final @PathVariable int id) {
        return postService.getPost(id);
    }

    @GetMapping(value = "/post/my", params = {"offset", "limit", "status"})
    public final ResponseEntity<PostsMyResponse> getMyPosts(
            final @RequestParam(value = "offset") int offset,
            final @RequestParam(value = "limit") int limit,
            final @RequestParam(value = "status") String status) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return postService.getMyPosts(offset, limit,
                status, user.getId());
    }

    @GetMapping(value = "/post/moderation",
            params = {"offset", "limit", "status"})
    public final ResponseEntity<?> getModerationPosts(
            final @RequestParam(value = "offset") int offset,
            final @RequestParam(value = "limit") int limit,
            final @RequestParam(value = "status") String status) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        if (Boolean.TRUE.equals(user.getIsModerator())) {
            return postService.getModeratePosts(offset, limit,
                    status, Math.toIntExact(user.getId()));
        } else {
            return new ResponseEntity<>("user not moderator",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/post")
    public final ResponseEntity<?> addPost(
            final @RequestBody PostAddRequest postAddRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return postService.addPost(postAddRequest, user);
    }

    @PutMapping(value = "/post/{id}")
    public final ResponseEntity<?> editPost(
            final @PathVariable int id,
            final @RequestBody PostAddRequest postAddRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return postService.editPost(id, postAddRequest, user);
    }

    @PostMapping(value = "/post/like")
    public final ResponseEntity<?> likePost(
            final @RequestBody PostVoteRequest postVoteRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return postVoteService.likePost(postVoteRequest, user);
    }

    @PostMapping(value = "/post/dislike")
    public final ResponseEntity<?> dislikePost(
            final @RequestBody PostVoteRequest postVoteRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return postVoteService.dislikePost(postVoteRequest, user);
    }
}
