package me.ldclrcq.filature.sources.connectors.ileo;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IleoConnector implements SourceConnector {
    private final static String BASE_URL = "https://www.mel-ileo.fr";
    private final static String BILL_URL = BASE_URL + "/espaceperso/gerer-factures.aspx";

    private final FileTempStorage fileTempStorage;
    private final PlaywrightConfiguration playwrightConfiguration;

    public IleoConnector(FileTempStorage fileTempStorage, PlaywrightConfiguration playwrightConfiguration) {
        this.fileTempStorage = fileTempStorage;
        this.playwrightConfiguration = playwrightConfiguration;
    }

    @Override
    public SourceType appliesTo() {
        return SourceType.ILEO;
    }

    @Override
    public DownloadResult downloadDocuments(Source source, LocalDateTime lastDocumentDownloadedDate) {
        var login = source.credentials.get("login");
        var password = source.credentials.get("password");

        try (var playwright = Playwright.create();
             var browser = playwright.chromium().launch(playwrightConfiguration.getLaunchOptions())) {
            var page = browser.newPage();
            page.navigate(BASE_URL);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Espace PERSO")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Adresse e-mail (ou ancien")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Adresse e-mail (ou ancien")).fill(login);
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Mot de passe")).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Mot de passe")).fill(password);
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("je me connecte")).click();

            page.navigate(BILL_URL);

            List<Locator> trs = page.locator("tr.selected").all();

            List<Path> downloadedBills = new ArrayList<>();
            LocalDate lastDocumentDownloadDate = lastDocumentDownloadedDate != null ? lastDocumentDownloadedDate.toLocalDate().withDayOfMonth(1) : null;
            LocalDate newLastDocumentDownloadDate = lastDocumentDownloadDate;

            for (Locator tr : trs) {
                String pdfUrl = tr.locator("a").getAttribute("href");

                if (pdfUrl == null || pdfUrl.isBlank()) {
                    Log.info("Skipping document: no PDF URL found");
                    continue;
                }

                String rawDate = tr.locator("td:nth-child(2)").innerText();
                LocalDate date = LocalDate.parse(rawDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if (lastDocumentDownloadDate != null && (date.isBefore(lastDocumentDownloadDate) || date.isEqual(lastDocumentDownloadDate))) {
                    Log.info("Skipping document: " + date + " (already downloaded)");
                    continue;
                }

                APIResponse downloadResponse = page.request().get(BASE_URL + pdfUrl);

                try (var buffer = new BufferedInputStream(new ByteArrayInputStream(downloadResponse.body()))) {
                    downloadedBills.add(fileTempStorage.storeTempDocument(this.appliesTo(), buffer, getFileName(date)));

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
        return String.format("%s_ileo.pdf", date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }
}
