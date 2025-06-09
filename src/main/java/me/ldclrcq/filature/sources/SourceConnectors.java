package me.ldclrcq.filature.sources;

import io.quarkus.arc.All;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SourceConnectors {
    private final Map<SourceType, SourceConnector> connectorsByType = new EnumMap<>(SourceType.class);

    public SourceConnectors(@All List<SourceConnector> connectors) {
        connectors.forEach(sourceConnector -> connectorsByType.put(sourceConnector.appliesTo(), sourceConnector));
    }

    public SourceConnector getForType(SourceType sourceType) {
        return connectorsByType.get(sourceType);
    }
}
