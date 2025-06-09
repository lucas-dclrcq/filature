package me.ldclrcq.filature.sources;

public enum SourceType {
    ENERCOOP("Enercoop"),
    FREE("Free");

    private final String name;

    SourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
