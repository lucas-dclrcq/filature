package me.ldclrcq.filature.notifiers.matrix;

import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.notifiers.Notifier;
import me.ldclrcq.filature.notifiers.NotifierType;
import me.ldclrcq.filature.synchronizations.SynchronizationSummary;

@ApplicationScoped
public class MatrixNotifier implements Notifier {
    @Override
    public boolean appliesTo(NotifierType notifierType) {
        return notifierType == NotifierType.MATRIX;
    }

    @Override
    public void notify(SynchronizationSummary synchronizationSummary) {

    }
}
