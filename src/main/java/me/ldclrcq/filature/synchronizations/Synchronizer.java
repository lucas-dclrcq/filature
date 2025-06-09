package me.ldclrcq.filature.synchronizations;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.configuration.FilatureConfiguration;
import me.ldclrcq.filature.connections.Connection;
import me.ldclrcq.filature.notifiers.NotifierConnectors;
import me.ldclrcq.filature.sources.SourceConnector;
import me.ldclrcq.filature.sources.SourceConnectors;
import me.ldclrcq.filature.targets.TargetConnector;
import me.ldclrcq.filature.targets.TargetConnectors;

import java.time.LocalDateTime;

@ApplicationScoped
public class Synchronizer {
    private final FilatureConfiguration configuration;
    private final SourceConnectors sourceConnectors;
    private final TargetConnectors targetConnectors;
    private final NotifierConnectors notifierConnectors;

    public Synchronizer(FilatureConfiguration configuration, SourceConnectors sourceConnectors, TargetConnectors targetConnectors, NotifierConnectors notifierConnectors) {
        this.configuration = configuration;
        this.sourceConnectors = sourceConnectors;
        this.targetConnectors = targetConnectors;
        this.notifierConnectors = notifierConnectors;
    }

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
            SourceConnector sourceConnector = this.sourceConnectors.getForType(connection.source.type);

            var downloadedDocuments = sourceConnector.downloadDocuments(connection.source, connection.lastDocumentDownloadedDate);

            if (!downloadedDocuments.isEmpty()) {
                TargetConnector targetConnector = this.targetConnectors.getForType(connection.target.type);
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

            if (connection.notifier != null) {
                this.notifierConnectors
                        .getForType(connection.notifier.type)
                        .notify(SynchronizationSummary.fromSynchronization(synchronization));
            }

            connection.lastSynchronizedAt = now;
            synchronization.persist();
            connection.persist();
        }
    }
}
