package main.services;

import main.api.request.RegisterRequest;
import main.api.response.AuthResponse;
import main.api.response.RegisterResponse;
import main.mapper.UserDTO;
import main.model.CaptchaCode;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserService {
    @Value("${user.password.validation_regex}")
    private String passwordValidationRegex;
    @Value("${user.name.validation_regex}")
    private String nameValidationRegex;
    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final UserDTO userDTO;
    private final RegisterResponse registerResponse;
    private final RegisterResponse.ErrorResponse error;
    private final AuthResponse authResponse;

    public UserService(final UserRepository userRepositoryParam,
                       final CaptchaRepository captchaRepositoryParam,
                       final UserDTO userDTOParam,
                       final RegisterResponse registerResponseParam,
                       final RegisterResponse.ErrorResponse errorParam,
                       final AuthResponse authResponseParam) {
        this.userRepository = userRepositoryParam;
        this.captchaRepository = captchaRepositoryParam;
        this.userDTO = userDTOParam;
        this.registerResponse = registerResponseParam;
        this.error = errorParam;
        this.authResponse = authResponseParam;
    }

    public final User getUser(final String name) {
        return userRepository.getUserByEmail(name);
    }

    public final ResponseEntity<?> register(
            final RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();
        String captcha = registerRequest.getCaptcha();
        String captchaSecret = registerRequest.getCaptchaSecret();
        ResponseEntity<RegisterResponse> response
                = badResponse(email, password, name, captcha, captchaSecret);
        if (response != null) {
            return response;
        }
        String hashedPassword = passwordEncoder(password);
        User user = userDTO.userToEntity(false, LocalDateTime.now(),
                name, email, hashedPassword);
        userRepository.save(user);
        authResponse.setResult(true);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private ResponseEntity<RegisterResponse> badResponse(
            final String email,
            final String password,
            final String name,
            final String captcha,
            final String captchaSecret) {
        boolean isUserExistsByEmail = userRepository
                .getUserByEmail(email.toLowerCase()) != null;
        boolean isNameValid = isNameValid(name);
        boolean isPassValid = isPasswordValid(password);
        boolean isCaptchaCodeValid = isCaptchaValid(captcha, captchaSecret);
        if (isUserExistsByEmail) {
            error.setEmail("Этот e-mail уже зарегистрирован");
            registerResponse.setResult(false);
            registerResponse.setErrors(error);
            return new ResponseEntity<>(registerResponse,
                    HttpStatus.BAD_REQUEST);
        }
        if (!isNameValid) {
            error.setName("Имя указано неверно");
            registerResponse.setResult(false);
            registerResponse.setErrors(error);
            return new ResponseEntity<>(registerResponse,
                    HttpStatus.BAD_REQUEST);
        }
        if (!isPassValid) {
            error.setPassword("Пароль короче 6-ти символов");
            registerResponse.setResult(false);
            registerResponse.setErrors(error);
            return new ResponseEntity<>(registerResponse,
                    HttpStatus.BAD_REQUEST);
        }
        if (!isCaptchaCodeValid) {
            error.setCaptcha("Код с картинки введён неверно");
            registerResponse.setResult(false);
            registerResponse.setErrors(error);
            return new ResponseEntity<>(registerResponse,
                    HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private Boolean isCaptchaValid(final String captcha,
                                   final String captchaSecret) {
        CaptchaCode captchaCode = captchaRepository
                .getCaptchaBySecretCode(captchaSecret);
        return captchaCode != null && captchaCode.getCode().equals(captcha);
    }

    private boolean isPasswordValid(final String passwordToCheck) {
        return (passwordToCheck != null
                && passwordToCheck.matches(passwordValidationRegex));
    }

    private boolean isNameValid(final String nameToCheck) {
        return (nameToCheck != null && nameToCheck
                .matches(nameValidationRegex));
    }

    private String passwordEncoder(final String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
