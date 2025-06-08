package me.ldclrcq.filature.synchronizations;

import io.quarkus.arc.All;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.ldclrcq.filature.configuration.FilatureConfiguration;
import me.ldclrcq.filature.connections.Connection;
import me.ldclrcq.filature.sources.SourceConnector;
import me.ldclrcq.filature.sources.SourceType;
import me.ldclrcq.filature.targets.TargetConnector;
import me.ldclrcq.filature.targets.TargetType;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class Synchronizer {
    @Inject
    @All
    List<SourceConnector> sourceConnectors;

    @Inject
    @All
    List<TargetConnector> targetConnectors;

    @Inject
    FilatureConfiguration configuration;

    @Scheduled(every = "{filature.synchronize-every}")
    public void scheduledSynchronize() {
        Connection.findSynchronizedMoreThan(configuration.synchronizeOlderThanHours())
                .ifPresentOrElse(this::synchronize, () -> Log.info("No connections to synchronize"));
    }

    public void synchronizeAllForUser(String userId) {
        Connection.findForUser(userId)
                .forEach(this::synchronize);
    }

    public void synchronizeNow(String userId, int connectionId) {
        Connection.findById(userId, connectionId)
                .ifPresent(this::synchronize);
    }

    private void synchronize(Connection connection) {
        Log.info("Starting synchronization for connection " + connection.id + " (" + connection.source.type + " -> " + connection.target.type + ") with upload folder " + connection.targetUploadPath + " ...");
        var synchronization = new Synchronization(LocalDateTime.now(), connection);

        try {
            SourceConnector sourceConnector = getSourceConnector(connection.source.type);

            var downloadedDocuments = sourceConnector.downloadDocuments(connection.source, connection.lastDocumentDownloadedDate);

            if (!downloadedDocuments.isEmpty()) {
                TargetConnector targetConnector = getTargetConnector(connection.target.type);
                targetConnector.uploadDocuments(connection.target, downloadedDocuments.downloadedPaths(), connection.targetUploadPath);

                connection.source.configuration.put("lastDocumentDate", downloadedDocuments.lastDocumentDate().toString());
            } else {
                Log.info("No new documents to synchronize for connection " + connection.id + " (" + connection.source.type + " -> " + connection.target.type + ")");
            }
            synchronization.status = SynchronizationStatus.SUCCESS;
            Log.info("Synchronization for connection " + connection.id + " (" + connection.source.type + " -> " + connection.target.type + ") completed successfully");
        } catch (Exception e) {
            Log.error("Error during synchronization for connection " + connection.id + " (" + connection.source.type + " -> " + connection.target.type + ")", e);
            synchronization.error = e.getMessage();
            synchronization.status = SynchronizationStatus.FAILURE;
        }
        finally {
            LocalDateTime now = LocalDateTime.now();
            synchronization.endedAt = now;
            connection.lastSynchronizedAt = now;
            synchronization.persist();
            connection.persist();
        }
    }

    public SourceConnector getSourceConnector(SourceType sourceType) {
        return sourceConnectors.stream()
                .filter(connector -> connector.appliesTo(sourceType))
                .findFirst()
                .orElseThrow();
    }

    public TargetConnector getTargetConnector(TargetType targetType) {
        return targetConnectors.stream()
                .filter(connector -> connector.appliesTo(targetType))
                .findFirst()
                .orElseThrow();
    }
}
