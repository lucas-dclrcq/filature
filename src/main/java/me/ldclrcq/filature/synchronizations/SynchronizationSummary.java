package me.ldclrcq.filature.synchronizations;

import java.time.LocalDateTime;

public record SynchronizationSummary(Long id, Long connectionId, SynchronizationStatus status, String error, LocalDateTime startedAt, LocalDateTime endedAt) {
    public static SynchronizationSummary fromSynchronization(Synchronization synchronization) {
        return new SynchronizationSummary(synchronization.id, synchronization.connection.id, synchronization.status, synchronization.error, synchronization.startedAt, synchronization.endedAt);
    }
}
