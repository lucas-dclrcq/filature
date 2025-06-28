package me.ldclrcq.filature.targets.nextcloud.client;

import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;

public interface NextcloudClient {
    @PUT
    @Path("{filename}")
    void uploadFile(@PathParam("filename") String filename, InputStream file, @HeaderParam("Content-Length") long contentLength);

    @MKCOL
    void createFolder();

    @PROPFIND
    Response listFiles();
}
