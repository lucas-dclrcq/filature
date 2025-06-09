package me.ldclrcq.filature.notifiers;

import me.ldclrcq.filature.synchronizations.SynchronizationSummary;

public interface NotifierConnector {
    NotifierType appliesTo();
    void notify(SynchronizationSummary synchronizationSummary);
}
