package me.ldclrcq.filature.sources;

public record SourceSummary(Long id, String name, String category) {
    public static SourceSummary fromSource(Source source) {
        return new SourceSummary(source.id, source.type.name, source.type.category);
    }
}
