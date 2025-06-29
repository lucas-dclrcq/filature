package me.ldclrcq.filature.sources.connectors.free;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Playwright;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.sources.DownloadResult;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.sources.SourceConnector;
import me.ldclrcq.filature.sources.SourceType;
import me.ldclrcq.filature.sources.playwright.PlaywrightConfiguration;
import me.ldclrcq.filature.temp_storage.FileTempStorage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FreeConnector implements SourceConnector {
    private static final String LOGIN_URL = "https://subscribe.free.fr/login/do_login.pl";
    private static final String BILL_URL = "https://adsl.free.fr";

    private final FileTempStorage fileTempStorage;
    private final PlaywrightConfiguration playwrightConfiguration;

    public FreeConnector(FileTempStorage fileTempStorage, PlaywrightConfiguration playwrightConfiguration) {
        this.fileTempStorage = fileTempStorage;
        this.playwrightConfiguration = playwrightConfiguration;
    }


    @Override
    public SourceType appliesTo() {
        return SourceType.FREE;
    }

    @Override
    public DownloadResult downloadDocuments(Source source, LocalDateTime lastDocumentDownloadedDate) {
        var login = source.credentials.get("login");
        var password = source.credentials.get("password");

        try (var playwright = Playwright.create();
             var browser = playwright.chromium().launch(playwrightConfiguration.getLaunchOptions())) {
            var page = browser.newPage();
            page.navigate(LOGIN_URL);
            page.locator("input[id='login_b']").fill(login);
            page.locator("input[id='pass_b']").fill(password);
            page.locator("button[id='ok']").click();
            page.waitForSelector("a[title='Voir toutes mes factures']");
            page.locator("a[title='Voir toutes mes factures']").click();
            List<Locator> all = page.locator("ul.pane li").all();

            List<Path> downloadedBills = new ArrayList<>();
            LocalDate lastDocumentDownloadDate = lastDocumentDownloadedDate != null ? lastDocumentDownloadedDate.toLocalDate().withDayOfMonth(1) : null;
            LocalDate newLastDocumentDownloadDate = lastDocumentDownloadDate;

            for (Locator downloadLink : all) {
                String pdfUrl = downloadLink.locator("a").getAttribute("href");

                String month = pdfUrl.split("&")[2].split("=")[1];
                YearMonth yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyyMM"));
                LocalDate date = yearMonth.atDay(1);

                if (lastDocumentDownloadDate != null && (date.isBefore(lastDocumentDownloadDate) || date.isEqual(lastDocumentDownloadDate))) {
                    Log.info("Skipping document: " + date + " (already downloaded)");
                    continue;
                }

                APIResponse downloadResponse = page.request().get(BILL_URL + "/" + pdfUrl);

                try (var buffer = new BufferedInputStream(new ByteArrayInputStream(downloadResponse.body()))) {
                    downloadedBills.add(fileTempStorage.storeTempDocument(SourceType.FREE, buffer, getFileName(date)));

                    if (newLastDocumentDownloadDate == null || date.isAfter(newLastDocumentDownloadDate)) {
                        newLastDocumentDownloadDate = date;
                    }

                    Log.info("Downloaded document: " + date);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            LocalDateTime lastDocumentDate = newLastDocumentDownloadDate == null ? null : newLastDocumentDownloadDate.atStartOfDay();
            return new DownloadResult(downloadedBills, lastDocumentDate);
        }
    }

    private String getFileName(LocalDate date) {
        return String.format("%s_free.pdf", date.format(DateTimeFormatter.ofPattern("yyyyMM")));
    }
}