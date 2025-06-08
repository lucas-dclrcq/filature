package me.ldclrcq.filature.sources;

import java.time.LocalDateTime;

public interface SourceConnector {
    boolean appliesTo(SourceType sourceType);
    DownloadResult downloadDocuments(Source source, LocalDateTime lastDocumentDownloadedDate);
}
