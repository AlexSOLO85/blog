package main.services;

import lombok.RequiredArgsConstructor;
import main.api.response.BadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UploadService {
    @Value("${post.image.upload_folder}")
    private String imagesUploadFolder;
    @Value("${post.image.format}")
    private String imagesFormat;
    private static final String DELIMITER = "/";
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH_FOLDER_NAME = 2;
    private static final int LENGTH_IMAGE_NAME = 5;
    private final FilesService filesService;

    public final ResponseEntity<?> uploadImage(final MultipartFile image) {
        if (!Files.exists(Path.of(imagesUploadFolder))) {
            filesService.createDirectoriesByPath(imagesUploadFolder);
        }
        String fileDestPath;
        String directoryPath = getRandomDirectoryToUpload();
        if (Boolean.TRUE.equals(filesService
                .createDirectoriesByPath(directoryPath))) {
            String imageName = getRandomImageName();
            fileDestPath = directoryPath + DELIMITER + imageName;
            while (Files.exists(Path.of(fileDestPath))) {
                imageName = getRandomImageName();
                fileDestPath = directoryPath + DELIMITER + imageName;
            }
            filesService.copyMultiPartFileToPath(image,
                    Paths.get(directoryPath, imageName));
        } else {
            BadResponse badResponse = new BadResponse();
            BadResponse.Errors error = new BadResponse.Errors();
            error.setImage("Размер файла превышает допустимый размер");
            badResponse.setResult(false);
            badResponse.setErrors(error);
            return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(fileDestPath, HttpStatus.OK);
    }

    private String getRandomDirectoryToUpload() {
        String firstFolder = generateRandom(LENGTH_FOLDER_NAME);
        String secondFolder = generateRandom(LENGTH_FOLDER_NAME);
        String thirdFolder = generateRandom(LENGTH_FOLDER_NAME);
        return imagesUploadFolder
                + DELIMITER + firstFolder
                + DELIMITER + secondFolder
                + DELIMITER + thirdFolder;
    }

    private String getRandomImageName() {
        String imageName =
                String.valueOf(
                        generateRandom(LENGTH_IMAGE_NAME).hashCode());
        return imageName + "." + imagesFormat;
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
