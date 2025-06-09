package me.ldclrcq.filature.sources.connectors.free;

import jakarta.validation.constraints.NotBlank;
import me.ldclrcq.filature.sources.Source;

import java.util.HashMap;
import java.util.Map;

public class FreeSourceUpdateRequest {
    @NotBlank
    public String login;
    @NotBlank
    public String password;

    public void updateSource(Source source) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);
        source.credentials = credentials;
    }
}