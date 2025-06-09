package me.ldclrcq.filature.sources;

import java.time.LocalDateTime;

public interface SourceConnector {
    SourceType appliesTo();
    DownloadResult downloadDocuments(Source source, LocalDateTime lastDocumentDownloadedDate);
}
