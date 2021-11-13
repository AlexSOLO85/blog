package main.services;

import main.api.request.LoginRequest;
import main.api.response.LoginResponse;
import main.repository.PostRepository;
import main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostRepository postRepository;

    public LoginService(final AuthenticationManager authenticationManagerParam,
                        final UserRepository userRepositoryParam,
                        final AuthService authServiceParam,
                        final PostRepository postRepositoryParam) {
        this.authenticationManager = authenticationManagerParam;
        this.userRepository = userRepositoryParam;
        this.authService = authServiceParam;
        this.postRepository = postRepositoryParam;
    }

    public final ResponseEntity<LoginResponse> login(
            final LoginRequest loginRequest) {
        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();
        return new ResponseEntity<>(getLoginResponse((user.getUsername())),
                HttpStatus.OK);
    }

    public final ResponseEntity<?> check(final Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(authService.getAuthCheck(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<>(getLoginResponse(principal.getName()),
                HttpStatus.OK);
    }

    private LoginResponse getLoginResponse(final String email) {
        main.model.User currentUser =
                userRepository.findByEmail(email).orElseThrow(() ->
                        new UsernameNotFoundException(email));
        LoginResponse.UserLoginResponse userResponse =
                new LoginResponse.UserLoginResponse();
        userResponse.setId(Math.toIntExact(currentUser.getId()));
        userResponse.setName(currentUser.getName());
        userResponse.setPhoto(currentUser.getPhoto());
        userResponse.setEmail(currentUser.getEmail());
        userResponse.setModeration(Boolean.TRUE
                .equals(currentUser.getIsModerator()));
        if (Boolean.TRUE.equals(currentUser.getIsModerator())) {
            userResponse.setSettings(true);
            userResponse.setModerationCount(postRepository
                    .countModeratePosts());
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResult(true);
        loginResponse.setUserLoginResponse(userResponse);
        return loginResponse;
    }
}
