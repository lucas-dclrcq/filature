package me.ldclrcq.filature.targets;

import java.nio.file.Path;
import java.util.List;

public interface TargetConnector {
    TargetType appliesTo();
    void uploadDocuments(Target target, List<Path> downloadedDocuments, String uploadFolder);
}
