package me.ldclrcq.filature.sources.connectors.free;

import me.ldclrcq.filature.sources.Source;

public record FreeSourceSummary(Long id, String name, String login) {
    public static FreeSourceSummary fromSource(Source source) {
        return new FreeSourceSummary(source.id, source.type.getName(), source.credentials.get("login"));
    }
}