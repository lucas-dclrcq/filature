package me.ldclrcq.filature.connections;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.targets.Target;

import java.util.Optional;

public record ConnectionCreationRequest(
        @NotNull(message = "Source configuration ID is required") Long sourceConfigurationId,
        @NotNull(message = "Target configuration ID is required") Long targetConfigurationId,
        @NotBlank(message = "Target upload path is required") String targetUploadPath) {

    public Optional<Connection> toConnection(String userId) {
        Optional<Source> sourceConfiguration = Source.findByIdAndUser(sourceConfigurationId, userId);
        Optional<Target> targetConfiguration = Target.findByIdAndUser(targetConfigurationId, userId);

        if (sourceConfiguration.isEmpty() || targetConfiguration.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new Connection(
                userId,
                sourceConfiguration.get(),
                targetConfiguration.get(),
                targetUploadPath
        ));
    }
}