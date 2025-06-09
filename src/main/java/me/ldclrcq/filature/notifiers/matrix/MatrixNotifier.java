package me.ldclrcq.filature.notifiers.matrix;

import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.notifiers.Notifier;
import me.ldclrcq.filature.synchronizations.SynchronizationSummary;

@ApplicationScoped
public class MatrixNotifier implements Notifier {
    @Override
    public void notify(SynchronizationSummary synchronizationSummary) {

    }
}
