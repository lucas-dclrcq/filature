package me.ldclrcq.filature.sources.connectors.free;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.sources.DownloadResult;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.sources.SourceConnector;
import me.ldclrcq.filature.sources.SourceType;
import me.ldclrcq.filature.sources.temp_storage.FileTempStorage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class FreeConnector implements SourceConnector {
    private static final String LOGIN_URL = "https://subscribe.free.fr/login/do_login.pl";
    private static final String BILL_URL = "https://adsl.free.fr/liste-factures.pl";

    private final FileTempStorage fileTempStorage;

    public FreeConnector(FileTempStorage fileTempStorage) {
        this.fileTempStorage = fileTempStorage;
    }

    @Override
    public boolean appliesTo(SourceType sourceType) {
        return sourceType == SourceType.FREE;
    }

    @Override
    public DownloadResult downloadDocuments(Source source, LocalDateTime lastDocumentDownloadedDate) {
        var login = source.credentials.get("login");
        var password = source.credentials.get("password");

        try {
            var cookies = this.authenticate(login, password);
            var freeBills = this.fetchBills(cookies);

            var billsToDownload = Optional.ofNullable(lastDocumentDownloadedDate)
                    .map(thresholdDate -> freeBills.stream()
                            .filter(freeBill -> freeBill.getDate().isAfter(thresholdDate.toLocalDate()))
                            .toList())
                    .orElse(freeBills);

            if (billsToDownload.isEmpty()) {
                return DownloadResult.empty();
            }

            List<Path> paths = this.downloadBills(billsToDownload, cookies);

            FreeBill latestBill = billsToDownload.stream()
                    .max(Comparator.comparing(FreeBill::getDate))
                    .orElseThrow();

            return new DownloadResult(paths, latestBill.getDate().atStartOfDay());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Path> downloadBills(List<FreeBill> bills, Map<String, String> cookies) throws IOException {
        var paths = new ArrayList<Path>();
        for (FreeBill bill : bills) {
            paths.add(downloadBill(bill, cookies));
        }
        return paths;
    }

    private Path downloadBill(FreeBill bill, Map<String, String> cookies) throws IOException {
        String fileUrl = bill.getFileurl();
        String filename = bill.getFilename();

        var response = Jsoup.connect(fileUrl)
                .cookies(cookies)
                .ignoreContentType(true)
                .execute();

        var filePath = fileTempStorage.storeTempDocument(SourceType.FREE, response.bodyStream(), filename);

        Log.info("Downloaded bill: " + filePath);

        return filePath;
    }

    private Map<String, String> authenticate(String login, String password) throws IOException {
        Connection.Response loginResponse = Jsoup.connect(LOGIN_URL)
                .data("login", login)
                .data("pass", password)
                .data("link", "")
                .method(Connection.Method.POST)
                .followRedirects(false)
                .execute();

        if (loginResponse.statusCode() != 302) {
            throw new IOException("Login failed: No redirection after login");
        }

        String firstLocation = loginResponse.header("Location");
        if (firstLocation == null) {
            throw new IOException("LOGIN_FAILED_NO_LOCATION");
        }

        if (firstLocation.contains("error")) {
            throw new IOException("LOGIN_FAILED_ERROR_PAGE");
        }

        var pongResponse = Jsoup.connect(firstLocation)
                .cookies(loginResponse.cookies())
                .method(Connection.Method.GET)
                .followRedirects(false)
                .execute();

        String finalLocation = loginResponse.header("Location");
        if (finalLocation == null) {
            throw new IOException("LOGIN_FAILED_NO_LOCATION");
        }

        // Request the bill page
        Connection.Response billPageResponse = Jsoup.connect(finalLocation)
                .cookies(pongResponse.cookies())
                .method(Connection.Method.GET)
                .execute();

        if (billPageResponse.body().contains("Session invalide")) {
            throw new IOException("INVALID_SESSION");
        }

        return loginResponse.cookies();
    }

    private List<FreeBill> fetchBills(Map<String, String> cookies) throws IOException {
        List<FreeBill> bills = new ArrayList<>();

        // Get the bill page
        Document billPage = Jsoup.connect(BILL_URL)
                .cookies(cookies)
                .get();

        // Parse the bill list
        Elements billElements = billPage.select(".pane li");
        for (Element billElement : billElements) {
            try {
                // Extract amount
                String amountText = billElement.select(".last").text();
                amountText = amountText.replace(" Euros", "").replace("&euro;", "");
                amountText = amountText.replace(",", ".").trim();
                double amount = Double.parseDouble(amountText);

                // Extract PDF URL
                String pdfUrl = billElement.select("a").attr("href");
                pdfUrl = "https://adsl.free.fr/" + pdfUrl;

                // Extract month and date
                String month = pdfUrl.split("&")[2].split("=")[1];
                YearMonth yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyyMM"));
                LocalDate date = yearMonth.atDay(1);

                // Extract bill ID and contract number
                String idBill = pdfUrl.split("&")[3].split("=")[1];
                String contractNumber = pdfUrl.split("=")[1].split("&")[0];

                // Create bill object
                FreeBill bill = new FreeBill();
                bill.setAmount(amount);
                bill.setDate(date);
                bill.setVendor("Free");
                bill.setVendorRef(idBill);
                bill.setContractReference(contractNumber);
                bill.setFileurl(pdfUrl);
                bill.setFilename(getFileName(date));

                // Set file attributes
                Map<String, Object> fileAttributes = new HashMap<>();
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("datetime", date);
                metadata.put("datetimeLabel", "issueDate");
                metadata.put("contentAuthor", "free");
                metadata.put("issueDate", date);
                metadata.put("invoiceNumber", idBill);
                metadata.put("contractReference", contractNumber);
                metadata.put("isSubscription", true);
                metadata.put("carbonCopy", true);
                metadata.put("qualification", "isp_invoice");
                fileAttributes.put("metadata", metadata);
                bill.setFileAttributes(fileAttributes);


                if (date.getYear() > 2011) {
                    bills.add(bill);
                }
            } catch (Exception e) {
                Log.warn("Failed to parse bill: " + e.getMessage());
            }
        }

        return bills;
    }

    private String getFileName(LocalDate date) {
        return String.format("%s_free.pdf", date.format(DateTimeFormatter.ofPattern("yyyyMM")));
    }
}