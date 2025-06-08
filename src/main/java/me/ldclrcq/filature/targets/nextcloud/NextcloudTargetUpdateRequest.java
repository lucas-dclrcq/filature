package me.ldclrcq.filature.targets.nextcloud;

import jakarta.validation.constraints.NotBlank;
import me.ldclrcq.filature.targets.Target;

import java.util.Map;

public record NextcloudTargetUpdateRequest(
        @NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Password is required") String password,
        @NotBlank(message = "URL is required") String url) {

    public Target updateTarget(Target target) {
        target.credentials = Map.of("username", username, "password", password);
        target.configuration = Map.of("url", url);
        return target;
    }
}
