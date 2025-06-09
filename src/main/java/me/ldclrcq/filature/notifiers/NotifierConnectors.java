package me.ldclrcq.filature.notifiers;

import io.quarkus.arc.All;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class NotifierConnectors {
    private final Map<NotifierType, NotifierConnector> notifiersByType = new EnumMap<>(NotifierType.class);

    public NotifierConnectors(@All List<NotifierConnector> connectors) {
        connectors.forEach(notifierConnector -> notifiersByType.put(notifierConnector.appliesTo(), notifierConnector));
    }

    public NotifierConnector getForType(NotifierType sourceType) {
        return notifiersByType.get(sourceType);
    }
}
