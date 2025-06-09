package me.ldclrcq.filature.sources.connectors.free;

import jakarta.validation.constraints.NotBlank;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.sources.SourceType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FreeSourceCreationRequest {
    @NotBlank
    public String login;
    @NotBlank
    public String password;

    public Source toSource(String userId) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("login", login);
        credentials.put("password", password);

        return new Source(userId, SourceType.FREE, credentials, Collections.emptyMap());
    }
}