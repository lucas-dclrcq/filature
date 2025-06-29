package me.ldclrcq.filature.configuration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "filature")
public interface FilatureConfiguration {
    @WithDefault("/tmp/downloads")
    String tempDownloadPath();
    @WithDefault("24")
    int synchronizeOlderThanHours();
    @WithDefault("5m")
    String synchronizeEvery();

    Playwright playwright();

    interface Playwright {
        @WithDefault("true")
        boolean headless();

        @WithDefault("true")
        boolean disableGpu();
    }
}
