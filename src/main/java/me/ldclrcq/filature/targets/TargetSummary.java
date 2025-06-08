package me.ldclrcq.filature.targets;

public record TargetSummary(Long id, String name) {
    public static TargetSummary fromTarget(Target target) {
        return new TargetSummary(target.id, target.type.label);
    }
}