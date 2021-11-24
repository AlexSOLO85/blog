package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.ChangePassRequest;
import main.api.request.LoginRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestorePassRequest;
import main.api.response.BooleanResponse;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.services.CaptchaService;
import main.services.LoginService;
import main.services.LogoutService;
import main.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class ApiAuthController {
    private final CaptchaService captchaService;
    private final UserService userService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    @GetMapping("/check")
    public final ResponseEntity<?> authCheckResponse(
            final Principal principal) {
        return loginService.check(principal);
    }

    @GetMapping("/captcha")
    public final ResponseEntity<CaptchaResponse> captchaResponse() {
        return captchaService.generateCaptcha();
    }

    @PostMapping("/register")
    public final ResponseEntity<?> register(
            final @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public final ResponseEntity<LoginResponse> login(
            final @RequestBody LoginRequest loginRequest) {
        return loginService.login(loginRequest);
    }

    @GetMapping("/logout")
    public final ResponseEntity<BooleanResponse> logout(
            final HttpServletRequest request,
            final HttpServletResponse response) {
        return logoutService.logout(request, response);
    }

    @PostMapping("/restore")
    public final ResponseEntity<?> restorePassword(
            final @RequestBody RestorePassRequest restorePassRequest) {
        return userService.restorePassword(restorePassRequest);
    }

    @PostMapping("/password")
    public final ResponseEntity<?> changePassword(
            final @RequestBody ChangePassRequest changePassRequest) {
        return userService.changePassword(changePassRequest);
    }
}
