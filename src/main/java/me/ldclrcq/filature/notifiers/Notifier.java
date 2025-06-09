package me.ldclrcq.filature.notifiers;

import me.ldclrcq.filature.synchronizations.SynchronizationSummary;

public interface Notifier {
    void notify(SynchronizationSummary synchronizationSummary);
}
