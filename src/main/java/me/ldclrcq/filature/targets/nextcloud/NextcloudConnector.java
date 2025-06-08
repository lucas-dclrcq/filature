package me.ldclrcq.filature.targets.nextcloud;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriBuilder;
import me.ldclrcq.filature.targets.TargetType;
import me.ldclrcq.filature.targets.Target;
import me.ldclrcq.filature.targets.TargetConnector;
import org.apache.http.client.utils.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class NextcloudConnector implements TargetConnector {
    @Override
    public boolean appliesTo(TargetType targetType) {
        return targetType == TargetType.NEXTCLOUD;
    }

    @Override
    public void uploadDocuments(Target target, List<Path> downloadedDocuments, String uploadFolder) {
        var username = target.credentials.get("username");
        var password = target.credentials.get("password");
        var url = target.configuration.get("url");

        try {
            this.uploadBills(downloadedDocuments, username, password, uploadFolder, url);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void uploadBills(List<Path> billPaths, String nextcloudUsername, String nextcloudPassword, String nextcloudUploadFolder, String nextcloudUrl) throws IOException, URISyntaxException {
        for (Path billPath : billPaths) {
            uploadBill(billPath, nextcloudUsername, nextcloudPassword, nextcloudUploadFolder, nextcloudUrl);
        }
    }

    private void uploadBill(Path path, String nextcloudUsername, String nextcloudPassword, String nextcloudUploadFolder, String nextcloudUrl) throws IOException, URISyntaxException {
        File file = path.toFile();
        if (!file.exists() || file.length() == 0) {
            Log.info("Skipping upload of empty or non-existent file: " + path);
            return;
        }

        Sardine sardine = SardineFactory.begin(nextcloudUsername, nextcloudPassword);

        String folderUrl = UriBuilder.fromUri(nextcloudUrl).path("/remote.php/dav/files").path(nextcloudUsername).path(nextcloudUploadFolder).build().toString();
        if (!sardine.exists(folderUrl)) {
            sardine.createDirectory(folderUrl);
            Log.info("Created Nextcloud directory: " + folderUrl);
        }

        String uploadUrl = UriBuilder.fromUri(folderUrl).path(file.getName()).build().toString();
        sardine.put(uploadUrl, file, "application/pdf");
        Log.info("Uploaded bill to Nextcloud: " + uploadUrl);
    }
}
