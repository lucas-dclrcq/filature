package me.ldclrcq.filature.targets.nextcloud;

import io.quarkus.logging.Log;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriBuilder;
import me.ldclrcq.filature.targets.Target;
import me.ldclrcq.filature.targets.TargetConnector;
import me.ldclrcq.filature.targets.TargetType;
import me.ldclrcq.filature.targets.nextcloud.client.BasicAuthFilter;
import me.ldclrcq.filature.targets.nextcloud.client.NextcloudClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class NextcloudConnector implements TargetConnector {

    @Override
    public TargetType appliesTo() {
        return TargetType.NEXTCLOUD;
    }

    @Override
    public void uploadDocuments(Target target, List<Path> downloadedDocuments, String uploadFolder) {
        var username = target.credentials.get("username");
        var password = target.credentials.get("password");
        var url = target.configuration.get("url");


        try {
            NextcloudClient nextcloudClient = QuarkusRestClientBuilder.newBuilder()
                    .baseUri(UriBuilder.fromUri(url).path("/remote.php/dav/files").path(username).path(uploadFolder).build())
                    .register(new BasicAuthFilter(username, password))
                    .build(NextcloudClient.class);

            this.uploadBills(downloadedDocuments, username, uploadFolder, nextcloudClient);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void uploadBills(List<Path> billPaths, String nextcloudUsername, String nextcloudUploadFolder, NextcloudClient nextcloudUrl) throws IOException, URISyntaxException {
        for (Path billPath : billPaths) {
            uploadBill(billPath, nextcloudUsername, nextcloudUploadFolder, nextcloudUrl);
        }
    }

    private void uploadBill(Path path, String nextcloudUsername, String nextcloudUploadFolder, NextcloudClient nextcloudClient) throws IOException {
        File file = path.toFile();
        if (!file.exists() || file.length() == 0) {
            Log.info("Skipping upload of empty or non-existent file: " + path);
            return;
        }

        String folderPath = UriBuilder.fromUri(nextcloudUsername).path(nextcloudUploadFolder).build().toString();

        if (!folderExists(nextcloudClient)) {
            nextcloudClient.createFolder();
            Log.info("Created Nextcloud directory: " + folderPath);
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            nextcloudClient.uploadFile(file.getName(), bis, file.length());
            Log.info("Uploaded bill to Nextcloud: " + file.getName() + " (Size: " + file.length() + " bytes)");
        }
    }

    private static boolean folderExists(NextcloudClient nextcloudClient) {
        try {
            nextcloudClient.listFiles();
            return true;
        } catch (ClientWebApplicationException e) {
            if (e.getResponse().getStatus() == 404) {
                return false;
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}
