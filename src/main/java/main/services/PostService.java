package main.services;

import main.api.response.PostResponse;
import main.mapper.PostDTO;
import main.model.Post;
import main.model.User;
import main.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final PostDTO postDTO;

    public PostService(final PostRepository postRepositoryParam,
                       final UserService userServiceParam,
                       final PostDTO postDTOParam) {
        this.postRepository = postRepositoryParam;
        this.userService = userServiceParam;
        this.postDTO = postDTOParam;
    }

    private Post getPostById(final int id) {
        return postRepository.findByIdPost(id);
    }

    public final ResponseEntity<PostResponse> getPost(final int id) {
        Post post = getPostById(id);
        Authentication loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication();
        String username = loggedInUser.getName();
        User user = userService.getUser(username);
        if (user == null
                || (post.getUser() != user && !user.getIsModerator())) {
            return new ResponseEntity<>(getPostNotLoginUser(post),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(postDTO.postResponseDto(post),
                HttpStatus.OK);
    }

    private PostResponse getPostNotLoginUser(final Post post) {
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
        return postDTO.postResponseDto(post);
    }
}
