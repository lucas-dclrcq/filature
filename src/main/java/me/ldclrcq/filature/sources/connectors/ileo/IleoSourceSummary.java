package me.ldclrcq.filature.sources.connectors.ileo;

import me.ldclrcq.filature.sources.Source;

public record IleoSourceSummary(Long id, String name, String login) {
    public static IleoSourceSummary fromSource(Source source) {
        return new IleoSourceSummary(source.id, source.type.name, source.credentials.get("login"));
    }
}