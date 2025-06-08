package me.ldclrcq.filature.sources;

public record SourceSummary(Long id, String name) {
    public static SourceSummary fromSource(Source source) {
        return new SourceSummary(source.id, source.type.getName());
    }
}
