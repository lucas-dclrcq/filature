package me.ldclrcq.filature.notifiers.matrix;

import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.notifiers.NotifierConnector;
import me.ldclrcq.filature.notifiers.NotifierType;
import me.ldclrcq.filature.synchronizations.SynchronizationSummary;

@ApplicationScoped
public class MatrixNotifierConnector implements NotifierConnector {
    @Override
    public NotifierType appliesTo() {
        return NotifierType.MATRIX;
    }

    @Override
    public void notify(SynchronizationSummary synchronizationSummary) {

    }
}
