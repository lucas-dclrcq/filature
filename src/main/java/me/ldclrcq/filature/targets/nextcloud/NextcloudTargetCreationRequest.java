package me.ldclrcq.filature.targets.nextcloud;

import jakarta.validation.constraints.NotBlank;
import me.ldclrcq.filature.targets.TargetType;
import me.ldclrcq.filature.targets.Target;

import java.util.Map;

public record NextcloudTargetCreationRequest(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "URL is required") String url) {

    public Target toTargetConfiguration(String userId) {
        return new Target(
                userId,
                TargetType.NEXTCLOUD,
                Map.of("username", username, "password", password),
                Map.of("url", url)
        );
    }
}
