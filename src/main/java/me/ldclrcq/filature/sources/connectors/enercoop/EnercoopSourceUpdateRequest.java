package me.ldclrcq.filature.sources.connectors.enercoop;

import jakarta.validation.constraints.NotBlank;
import me.ldclrcq.filature.sources.Source;

import java.util.Map;

public record EnercoopSourceUpdateRequest(@NotBlank(message = "Username is required") String username, @NotBlank(message = "Password is required") String password) {
    public Source updateSource(Source source) {
        source.credentials = Map.of("username", username, "password", password);
        return source;
    }
}