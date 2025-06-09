package me.ldclrcq.filature.targets;

import io.quarkus.arc.All;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TargetConnectors {
    private final Map<TargetType, TargetConnector> connectorsByType = new EnumMap<>(TargetType.class);

    public TargetConnectors(@All List<TargetConnector> connectors) {
        connectors.forEach(connector -> connectorsByType.put(connector.appliesTo(), connector));
    }

    public TargetConnector getForType(TargetType sourceType) {
        return connectorsByType.get(sourceType);
    }
}
