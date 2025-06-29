package me.ldclrcq.filature.sources.playwright;

import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.configuration.FilatureConfiguration;

import java.util.ArrayList;

import static com.microsoft.playwright.BrowserType.LaunchOptions;

@ApplicationScoped
public class PlaywrightConfiguration {
    private final FilatureConfiguration configuration;

    public PlaywrightConfiguration(FilatureConfiguration configuration) {
        this.configuration = configuration;
    }

    public LaunchOptions getLaunchOptions() {
        var args = new ArrayList<String>();

        if (configuration.playwright().disableGpu()) {
            args.add("--disable-gpu");
        }

        return new LaunchOptions()
                .setHeadless(configuration.playwright().headless())
                .setChromiumSandbox(false)
                .setChannel("")
                .setArgs(args);
    }
}
