package me.ldclrcq.filature.sources;

public enum SourceType {
    ENERCOOP("Enercoop", "Utility"),
    FREE("Free", "Utility"),
    ILEO("Ileo", "Utility");

    public final String name;
    public final String category;

    SourceType(String name, String category) {
        this.name = name;
        this.category = category;
    }
}
