package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.ChangePassRequest;
import main.api.request.EditProfilePhotoRequest;
import main.api.request.EditProfileRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestorePassRequest;
import main.api.response.BooleanResponse;
import main.api.response.StatisticsResponse;
import main.api.response.BadResponse;
import main.model.CaptchaCode;
import main.model.GlobalSettings;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.model.enumerated.ModerationStatus;
import main.repository.CaptchaRepository;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import main.repository.UserRepository;
import main.util.LocalTimeMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class UserService {
    @Value("${user.password.validation_regex}")
    private String passwordValidationRegex;
    @Value("${user.name.validation_regex}")
    private String nameValidationRegex;
    @Value("${user.image.max_size}")
    private int maxPhotoSizeInBytes;
    @Value("${user.image.upload_folder}")
    private String uploadFolder;
    @Value("${user.image.avatars_folder}")
    private String avatarsFolder;
    @Value("${user.image.avatars_width_size}")
    private int avatarsSizeWidth;
    @Value("${user.image.avatars_height_size}")
    private int avatarsSizeHeight;
    @Value("${user.image.format}")
    private String imageFormat;
    @Value("${user.password.restore_pass_message_string}")
    private String restorePassMessageString;
    @Value("${user.password.restore_message_subject}")
    private String restoreMessageSubject;
    private static final String DELIMITER = "/";
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz123456789";
    private static final int LENGTH_IMAGE_NAME = 5;
    private static final int LENGTH_RESTORE_CODE = 45;
    private final UserRepository userRepository;
    private final CaptchaRepository captchaRepository;
    private final PostRepository postRepository;
    private final PostVoteRepository postVoteRepository;
    private final FilesService filesService;
    private final SettingsService settingsService;
    private final JavaMailSender mailSender;

    public final User getUser(final String name) {
        return userRepository.getUserByEmail(name);
    }

    public final ResponseEntity<?> register(
            final RegisterRequest registerRequest) {
        boolean isMultiUserMode = false;
        for (GlobalSettings settings : settingsService
                .getAllGlobalSettingsSet()) {
            if (settings.getCode().equals("MULTIUSER_MODE")) {
                isMultiUserMode = settings.getValue().equals("YES");
            }
        }
        if (!isMultiUserMode) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String name = registerRequest.getName();
        String captcha = registerRequest.getCaptcha();
        String captchaSecret = registerRequest.getCaptchaSecret();
        String defaultPhoto = "src/main/resources/static/default-1.png";
        ResponseEntity<BadResponse> response
                = badRegisterResponse(email, password, name,
                captcha, captchaSecret);
        if (response != null) {
            return response;
        }
        String hashedPassword = passwordEncoder(password);
        User user = new User(false, LocalDateTime.now(ZoneOffset.UTC),
                name, email, hashedPassword, defaultPhoto);
        userRepository.save(user);
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }

    public final ResponseEntity<?> editProfile(
            final EditProfileRequest editProfileRequest,
            final User user) {
        Byte removePhoto = editProfileRequest.getRemovePhoto();
        String email = editProfileRequest.getEmail();
        String name = editProfileRequest.getName();
        String password = editProfileRequest.getPassword();
        boolean isNameValid;
        boolean isEmailValid;
        boolean isPassValid;
        isPassValid = isPassEditValid(user, password);
        isNameValid = isNameEditValid(user, name);
        isEmailValid = isEmailEditValid(user, email);
        isRemovePhotoByUser(user, removePhoto);
        if (editProfileRequest instanceof EditProfilePhotoRequest) {
            MultipartFile photo = ((EditProfilePhotoRequest) editProfileRequest)
                    .getPhoto();
            if (photo != null) {
                if (photo.getSize() > maxPhotoSizeInBytes) {
                    return getProfilePhotoBadResponse();
                } else {
                    setPhoto(user, photo);
                }
            }
        }
        if (!isNameValid || !isPassValid || !isEmailValid) {
            return getProfileBadResponse(isNameValid, isEmailValid,
                    isPassValid);
        }
        userRepository.save(user);
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }

    private void setPhoto(final User user, final MultipartFile photo) {
        String directoryPath = getDirectoryToUpload();
        String imageName = getRandomImageName();
        if (Boolean.TRUE.equals(filesService
                .createDirectoriesByPath(directoryPath))) {
            String fileDestPath = directoryPath + DELIMITER + imageName;
            while (Files.exists(Paths.get(fileDestPath))) {
                imageName = getRandomImageName();
                fileDestPath = directoryPath + DELIMITER + imageName;
            }
            try {
                Path path = Paths.get(directoryPath, imageName);
                photo.transferTo(path);
                ImageIO.write(
                        resizeImage(ImageIO.read(photo.getInputStream())),
                        imageFormat,
                        path.toFile());
                user.setPhoto(fileDestPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BufferedImage resizeImage(
            final BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(
                avatarsSizeWidth,
                avatarsSizeHeight,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0,
                avatarsSizeWidth,
                avatarsSizeHeight,
                null);
        graphics2D.dispose();
        return resizedImage;
    }

    private ResponseEntity<BadResponse> getProfilePhotoBadResponse() {
        BadResponse badResponse = new BadResponse();
        BadResponse.Errors error = new BadResponse.Errors();
        error.setName("Фото слишком большое, нужно не более 5 Мб");
        badResponse.setResult(false);
        badResponse.setErrors(error);
        return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<BadResponse> getProfileBadResponse(
            final boolean isNameValid,
            final boolean isEmailValid,
            final boolean isPassValid) {
        BadResponse badResponse = new BadResponse();
        BadResponse.Errors error = new BadResponse.Errors();
        error.setEmail(!isEmailValid ? "Этот e-mail уже зарегистрирован" : "");
        error.setName(!isNameValid ? "Имя указано неверно" : "");
        error.setPassword(!isPassValid ? "Пароль короче 6-ти символов" : "");
        badResponse.setResult(false);
        badResponse.setErrors(error);
        return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
    }

    private void isRemovePhotoByUser(final User user, final Byte removePhoto) {
        if (removePhoto != null && removePhoto == 1) {
            user.setPhoto("");
        }
    }

    private boolean isEmailEditValid(final User user, final String email) {
        boolean isEmailValid;
        boolean isAnotherUserExistsByEmail;
        if (email != null && !email.isBlank() && !email.equalsIgnoreCase(user
                .getEmail())) {
            isAnotherUserExistsByEmail =
                    !email.equalsIgnoreCase(user.getEmail()) && userRepository
                            .getUserByEmail(email.toLowerCase()) != null;
            isEmailValid = !isAnotherUserExistsByEmail;
            if (isEmailValid) {
                user.setEmail(email);
            }
        } else {
            isEmailValid = true;
        }
        return isEmailValid;
    }

    private boolean isNameEditValid(final User user, final String name) {
        boolean isNameValid;
        if (name != null && !name.isBlank() && !name.equals(user.getName())) {
            isNameValid = (userRepository.getUserByName(name) == null);
            if (isNameValid) {
                user.setName(name);
            }
        } else {
            isNameValid = true;
        }
        return isNameValid;
    }

    private boolean isPassEditValid(final User user, final String password) {
        boolean isPassValid;
        if (password != null && !password.isBlank()) {
            isPassValid = isPasswordValid(password);
            String hashedPassword = passwordEncoder(password);
            if (isPassValid) {
                user.setPassword(hashedPassword);
            }
        } else {
            isPassValid = true;
        }
        return isPassValid;
    }

    public final ResponseEntity<?> restorePassword(
            final RestorePassRequest request) {
        String email = request.getEmail();
        String restoreCode = generateRandom(LENGTH_RESTORE_CODE);
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(new BooleanResponse(false),
                    HttpStatus.OK);
        }
        user.setCode(restoreCode);
        userRepository.save(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(restoreMessageSubject);
        message.setText(restorePassMessageString + restoreCode);
        mailSender.send(message);
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }

    public final ResponseEntity<?> changePassword(
            final ChangePassRequest request) {
        String code = request.getCode();
        String password = request.getPassword();
        String captcha = request.getCaptcha();
        String captchaSecret = request.getCaptchaSecret();
        if (code == null || password == null || captcha == null
                || captchaSecret == null || code.isBlank() || password.isBlank()
                || captcha.isBlank() || captchaSecret.isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.getUserByCode(code);
        boolean isCodeValid = (user != null);
        boolean isPassValid = isPasswordValid(password);
        boolean isCaptchaCodeValid = isCaptchaValid(captcha, captchaSecret);
        ResponseEntity<BadResponse> response
                = badChangePassResponse(
                isCodeValid,
                isPassValid,
                isCaptchaCodeValid);
        if (response != null) {
            return response;
        }
        String newHashedPassword = passwordEncoder(password);
        Objects.requireNonNull(user).setPassword(newHashedPassword);
        userRepository.save(user);
        return new ResponseEntity<>(new BooleanResponse(true), HttpStatus.OK);
    }

    public final ResponseEntity<?> getMyStatistics(final User user) {
        LocalTimeMapper localTimeMapper = new LocalTimeMapper();
        long firstPostTime = 0;
        Set<Post> myPosts = user.getPosts().stream()
                .filter(p -> p.getModerationStatus()
                        .equals(ModerationStatus.ACCEPTED)
                        && p.getIsActive() && p.getTime()
                        .isBefore(LocalDateTime.now()))
                .collect(Collectors.toSet());
        int postsCount = myPosts.size();
        int allLikesCount = 0;
        int allDislikeCount = 0;
        int viewsCount = 0;
        for (Post post : myPosts) {
            long currentPostTime = localTimeMapper.dateToLong(post.getTime());
            if (firstPostTime == 0) {
                firstPostTime = currentPostTime;
            }
            if (firstPostTime > currentPostTime) {
                firstPostTime = currentPostTime;
            }
            viewsCount += post.getViewCount();
            List<PostVote> currentPostVotes = post.getPostVotes();
            for (PostVote like : currentPostVotes) {
                if (like.getValue() == 1) {
                    allLikesCount += 1;
                } else if (like.getValue() == -1) {
                    allDislikeCount += 1;
                }
            }
        }
        StatisticsResponse statisticsResponse =
                new StatisticsResponse(
                        postsCount,
                        allLikesCount,
                        allDislikeCount,
                        viewsCount,
                        firstPostTime);
        return new ResponseEntity<>(statisticsResponse.getMap(), HttpStatus.OK);
    }

    public final ResponseEntity<?> getAllStatistics(final User user) {
        boolean isStatisticsIsPublic = false;
        for (GlobalSettings settings : settingsService
                .getAllGlobalSettingsSet()) {
            if (settings.getCode().equals("STATISTICS_IS_PUBLIC")) {
                isStatisticsIsPublic = settings.getValue().equals("YES");
            }
        }
        if (!isStatisticsIsPublic && Boolean.TRUE
                .equals(!user.getIsModerator())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        LocalTimeMapper localTimeMapper = new LocalTimeMapper();
        int postsCount = postRepository.countAllPostsAtDatabase();
        int allLikesCount = postVoteRepository.countAllLikes();
        int allDislikeCount = postVoteRepository.countAllDislikes();
        int viewsCount = postRepository.countAllViews();
        long firstPublicationDate = localTimeMapper
                .dateToLong(postRepository.getFirstPublicationDate(PageRequest
                        .of(0, 1)));
        StatisticsResponse statisticsResponse =
                new StatisticsResponse(
                        postsCount,
                        allLikesCount,
                        allDislikeCount,
                        viewsCount,
                        firstPublicationDate);
        return new ResponseEntity<>(statisticsResponse.getMap(), HttpStatus.OK);
    }

    private ResponseEntity<BadResponse> badChangePassResponse(
            final boolean isCodeValid,
            final boolean isPassValid,
            final boolean isCaptchaCodeValid) {
        BadResponse badResponse = new BadResponse();
        BadResponse.Errors error = new BadResponse.Errors();
        if (!isCodeValid || !isPassValid || !isCaptchaCodeValid) {
            error.setCode(!isCodeValid
                    ? "Ссылка для восстановления пароля устарела. "
                    + "<a href=\"/login/restore-password\">"
                    + "Запросить ссылку снова</a>" : "");
            error.setPassword(!isPassValid
                    ? "Пароль короче 6-ти символов!" : "");
            error.setCaptcha(!isCaptchaCodeValid
                    ? "Код с картинки введён неверно" : "");
            badResponse.setResult(false);
            badResponse.setErrors(error);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private ResponseEntity<BadResponse> badRegisterResponse(
            final String email,
            final String password,
            final String name,
            final String captcha,
            final String captchaSecret) {
        BadResponse badResponse = new BadResponse();
        BadResponse.Errors error = new BadResponse.Errors();
        boolean isUserExistsByEmail = userRepository
                .getUserByEmail(email.toLowerCase()) != null;
        boolean isNameValid = isNameValid(name);
        boolean isPassValid = isPasswordValid(password);
        boolean isCaptchaCodeValid = isCaptchaValid(captcha, captchaSecret);
        if (isUserExistsByEmail || !isNameValid
                || !isPassValid || !isCaptchaCodeValid) {
            error.setEmail(isUserExistsByEmail
                    ? "Этот e-mail уже зарегистрирован" : "");
            error.setName(!isNameValid
                    ? "Имя указано неверно" : "");
            error.setPassword(!isPassValid
                    ? "Пароль короче 6-ти символов" : "");
            error.setCaptcha(!isCaptchaCodeValid
                    ? "Код с картинки введён неверно" : "");
            badResponse.setResult(false);
            badResponse.setErrors(error);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private Boolean isCaptchaValid(final String captcha,
                                   final String captchaSecret) {
        CaptchaCode captchaCode = captchaRepository
                .getBySecretCodeEquals(captchaSecret);
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

    private String getDirectoryToUpload() {
        return uploadFolder
                + DELIMITER
                + avatarsFolder;
    }

    private String getRandomImageName() {
        String imageName =
                String.valueOf(
                        generateRandom(LENGTH_IMAGE_NAME).hashCode());
        return imageName + "." + imageFormat;
    }

    private String generateRandom(final int len) {
        Random rnd = null;
        try {
            rnd = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Random finalRnd = rnd;
        return IntStream.range(0, len)
                .mapToObj(i -> {
                    assert finalRnd != null;
                    return String.valueOf(
                            CHARS.charAt(finalRnd.nextInt(CHARS.length())));
                })
                .collect(Collectors.joining());
    }
}
