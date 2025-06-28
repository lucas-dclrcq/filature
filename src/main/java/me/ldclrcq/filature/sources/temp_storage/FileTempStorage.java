package me.ldclrcq.filature.sources.temp_storage;

import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.configuration.FilatureConfiguration;
import me.ldclrcq.filature.sources.SourceType;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

@ApplicationScoped
public class FileTempStorage {

    private final FilatureConfiguration filatureConfiguration;

    public FileTempStorage(FilatureConfiguration filatureConfiguration) {
        this.filatureConfiguration = filatureConfiguration;
    }

    private void createTempDirectoryIfNotExist(SourceType sourceType) throws TempStorageException {
        Path downloadDir = Paths.get(filatureConfiguration.tempDownloadPath(), sourceType.name());
        if (!Files.exists(downloadDir)) {
            try {
                Files.createDirectories(downloadDir);
            } catch (IOException e) {
                throw new TempStorageException("Could not create temp directory for source " + sourceType.name, e);
            }
        }
    }

    public Path storeTempDocument(SourceType sourceType, BufferedInputStream inputStream, String filename) throws TempStorageException {
        this.createTempDirectoryIfNotExist(sourceType);
        Path filePath = Paths.get(filatureConfiguration.tempDownloadPath(), sourceType.name(), filename);
        try {
            Files.copy(inputStream, filePath);
            return filePath;
        } catch (IOException e) {
            throw new TempStorageException("Could not create temp document for filename " + filename, e);
        }
    }

    public void cleanTempFiles(SourceType type) throws IOException {
        Path tempDirectory = Paths.get(filatureConfiguration.tempDownloadPath(), type.name());
        if (Files.exists(tempDirectory)) {
            try (var paths = Files.walk(tempDirectory)) {
                paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        }
    }
}
