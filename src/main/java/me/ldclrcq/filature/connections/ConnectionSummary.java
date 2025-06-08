package me.ldclrcq.filature.connections;

import me.ldclrcq.filature.sources.SourceSummary;
import me.ldclrcq.filature.targets.TargetSummary;

public record ConnectionSummary(Long id, String userId, SourceSummary source, TargetSummary target, String targetUploadPath) {
    public static ConnectionSummary fromConnection(Connection connection) {
        return new ConnectionSummary(connection.id, connection.userId, SourceSummary.fromSource(connection.source), TargetSummary.fromTarget(connection.target), connection.targetUploadPath);
    }
}
