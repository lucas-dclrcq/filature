package me.ldclrcq.filature.sources.connectors.enercoop;

import me.ldclrcq.filature.sources.Source;

public record EnercoopSourceSummary(Long id, String name, String username) {
    public static EnercoopSourceSummary fromSource(Source source) {
        return new EnercoopSourceSummary(source.id, source.type.getName(), source.credentials.get("username"));
    }
}
