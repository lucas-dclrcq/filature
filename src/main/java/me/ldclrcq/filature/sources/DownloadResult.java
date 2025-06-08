package me.ldclrcq.filature.sources;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public record DownloadResult(List<Path> downloadedPaths, LocalDateTime lastDocumentDate) {
    public static DownloadResult empty() {
        return new DownloadResult(List.of(), null);
    }

    public boolean isEmpty() {
        return this.downloadedPaths.isEmpty();
    }
}
