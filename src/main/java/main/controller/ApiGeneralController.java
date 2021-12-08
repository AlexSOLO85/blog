package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.EditProfilePhotoRequest;
import main.api.request.EditProfileRequest;
import main.api.request.PostCommentRequest;
import main.api.request.PostModerateRequest;
import main.api.request.SettingsRequest;
import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.TagResponse;
import main.model.User;
import main.services.CalendarService;
import main.services.PostService;
import main.services.SettingsService;
import main.services.TagService;
import main.services.UploadService;
import main.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiGeneralController {
    private final InitResponse initResponse;
    private final SettingsService settingsService;
    private final TagService tagService;
    private final CalendarService calendarService;
    private final UploadService uploadService;
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/init")
    public final ResponseEntity<InitResponse> initResponse() {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("/settings")
    public final ResponseEntity<Map<String, Boolean>> getSettingsResponse() {
        return settingsService.getGlobalSettings();
    }

    @PutMapping("/settings")
    public final ResponseEntity<?> setGlobalSettings(
            final @RequestBody SettingsRequest settingsRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return settingsService.setGlobalSettings(settingsRequest, user);
    }

    @GetMapping(value = "/tag")
    public final ResponseEntity<TagResponse> tagResponse(
            final @RequestParam(defaultValue = "") String query) {
        return tagService.getTagWithQueryResponse(query);
    }

    @GetMapping(value = "/calendar", params = {"year"})
    public final ResponseEntity<CalendarResponse> calendarResponse(
            final @RequestParam(value = "year") String year) {
        return calendarService.countPostsByYear(year);
    }

    @PostMapping(value = "/image")
    public final ResponseEntity<?> uploadImage(
            final @RequestParam MultipartFile image) {
        return uploadService.uploadImage(image);
    }

    @PostMapping(value = "/comment")
    public final ResponseEntity<?> addComment(
            final @RequestBody PostCommentRequest postCommentRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return postService.addComment(postCommentRequest, user);
    }

    @PostMapping(value = "/moderation")
    public final ResponseEntity<?> moderatePost(
            final @RequestBody PostModerateRequest postModerateRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return postService.moderatePost(postModerateRequest, user);
    }

    @PostMapping(value = "/profile/my")
    public final ResponseEntity<?> editProfile(
            final @RequestBody EditProfileRequest request) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return userService.editProfile(request, user);
    }

    @PostMapping(value = "/profile/my",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public final ResponseEntity<?> editProfileWithPhoto(
            final @ModelAttribute EditProfilePhotoRequest request) {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return userService.editProfile(request, user);
    }

    @GetMapping(value = "/statistics/my")
    public final ResponseEntity<?> getMyStatistics() {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return userService.getMyStatistics(user);
    }

    @GetMapping(value = "/statistics/all")
    public final ResponseEntity<?> getAllStatistics() {
        Authentication loggedInUser = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = userService.getUser(loggedInUser.getName());
        return userService.getAllStatistics(user);
    }
}
