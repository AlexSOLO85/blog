package main.controller;

import lombok.RequiredArgsConstructor;
import main.service.FilesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor
public class FileController {
    private static final String DELIMITER = "/";
    private final FilesService filesService;

    @GetMapping({"/**/upload/avatars/{imageName}"})
    public final ResponseEntity<?> getAvatar(
            final @PathVariable("imageName") String imageName) {
        String pathToFile = "upload/avatars/" + imageName;
        return getResponseWithImage(pathToFile);
    }

    @GetMapping({"/**/src/main/resources/static/default-1.png"})
    public final ResponseEntity<?> getDefaultAvatar() {
        String pathToFile = "src/main/resources/static/default-1.png";
        return getResponseWithImage(pathToFile);
    }

    @GetMapping({"/**/upload/{subDir1}/{subDir2}/{subDir3}/{imageName}"})
    public final ResponseEntity<?> getImage(
            final @PathVariable(value = "imageName")
                    String imageName,
            final @PathVariable(value = "subDir1",
                    required = false) String subDir1,
            final @PathVariable(value = "subDir2",
                    required = false) String subDir2,
            final @PathVariable(value = "subDir3",
                    required = false) String subDir3) {
        String pathToFile = "upload/"
                + (subDir1 != null ? subDir1 + DELIMITER : "")
                + (subDir2 != null ? subDir2 + DELIMITER : "")
                + (subDir3 != null ? subDir3 + DELIMITER : "")
                + imageName;
        return getResponseWithImage(pathToFile);
    }

    private ResponseEntity<?> getResponseWithImage(final String pathToFile) {
        try {
            File imageFile = filesService.getFileByPath(pathToFile);
            byte[] image = Files.readAllBytes(imageFile.toPath());
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(FileTypeMap
                            .getDefaultFileTypeMap()
                            .getContentType(imageFile)))
                    .body(image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
