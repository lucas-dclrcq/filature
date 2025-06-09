package me.ldclrcq.filature.notifiers;

import me.ldclrcq.filature.synchronizations.SynchronizationSummary;

public interface Notifier {
    boolean appliesTo(NotifierType notifierType);
    void notify(SynchronizationSummary synchronizationSummary);
}
