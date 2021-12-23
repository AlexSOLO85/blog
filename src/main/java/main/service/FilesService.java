package main.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesService {
    public final Boolean createDirectoriesByPath(
            final String path) {
        try {
            Files.createDirectories(Path.of(path));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public final void copyMultiPartFileToPath(
            final MultipartFile source,
            final Path dest) {
        try {
            source.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final File getFileByPath(final String pathToFile)
            throws FileNotFoundException {
        if (Files.exists(Path.of(pathToFile))) {
            return new File(pathToFile);
        } else {
            throw new FileNotFoundException(
                    "По указанному пути отсутствует файл");
        }
    }
}
