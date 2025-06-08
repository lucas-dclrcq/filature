package me.ldclrcq.filature.targets.nextcloud;

import me.ldclrcq.filature.targets.Target;

public record NextcloudTargetSummary(Long id, String name, String username, String url) {
    public static NextcloudTargetSummary fromTarget(Target target) {
        return new NextcloudTargetSummary(target.id, target.type.name(), target.credentials.get("username"), target.configuration.get("url"));
    }
}