package me.ldclrcq.filature.targets;

import java.nio.file.Path;
import java.util.List;

public interface TargetConnector {
    boolean appliesTo(TargetType targetType);
    void uploadDocuments(Target target, List<Path> downloadedDocuments, String uploadFolder);
}
