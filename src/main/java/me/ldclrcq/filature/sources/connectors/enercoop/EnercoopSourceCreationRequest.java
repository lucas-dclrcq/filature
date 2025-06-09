package me.ldclrcq.filature.sources.connectors.enercoop;

import jakarta.validation.constraints.NotBlank;
import me.ldclrcq.filature.sources.SourceType;
import me.ldclrcq.filature.sources.Source;

import java.util.Collections;
import java.util.Map;

public record EnercoopSourceCreationRequest(@NotBlank(message = "Username is required") String username, @NotBlank(message = "Password is required") String password) {
    public Source toSourceConfiguration(String userId) {
        return new Source(userId, SourceType.ENERCOOP, Map.of("username", username, "password", password), Collections.emptyMap());
    }
}
