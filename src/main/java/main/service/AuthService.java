package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LoginRequest;
import main.api.response.BooleanResponse;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public final ResponseEntity<BooleanResponse> getAuthCheck() {
        return new ResponseEntity<>(new BooleanResponse(false),
                HttpStatus.OK);
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
            return new ResponseEntity<>(getAuthCheck(), HttpStatus.OK);
        }
        return new ResponseEntity<>(getLoginResponse(principal.getName()),
                HttpStatus.OK);
    }

    public final ResponseEntity<BooleanResponse> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        Authentication auth = SecurityContextHolder
                .getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
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
