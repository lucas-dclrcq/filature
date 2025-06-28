package me.ldclrcq.filature.sources.connectors.enercoop;

import jakarta.enterprise.context.ApplicationScoped;
import me.ldclrcq.filature.configuration.FilatureConfiguration;
import me.ldclrcq.filature.sources.DownloadResult;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.sources.SourceConnector;
import me.ldclrcq.filature.sources.SourceType;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@ApplicationScoped
public class EnercoopConnector implements SourceConnector {
    private static final String LOGIN_URL = "https://mon-espace.enercoop.fr/clients/sign_in";
    private static final String BILL_URL = "https://mon-espace.enercoop.fr/factures";
    private static final Locale LOCALE_FR = Locale.FRENCH;

    private final FilatureConfiguration filatureConfiguration;

    public EnercoopConnector(FilatureConfiguration filatureConfiguration) {
        this.filatureConfiguration = filatureConfiguration;
    }

    @Override
    public SourceType appliesTo() {
        return SourceType.ENERCOOP;
    }

    @Override
    public DownloadResult downloadDocuments(Source source, LocalDateTime lastDocumentDownloadedDate) {
//        var username = source.credentials.get("username");
//        var password = source.credentials.get("password");
//
//        try {
//            var cookies = this.authenticate(username, password);
//            var enercoopBills = this.fetchBills(cookies);
//
//            var billsToDownload = Optional.ofNullable(lastDocumentDownloadedDate)
//                    .map(thresholdDate -> enercoopBills.stream()
//                            .filter(enercoopBill -> enercoopBill.getDate().isAfter(thresholdDate.toLocalDate()))
//                            .toList())
//                    .orElse(enercoopBills);
//
//            if (billsToDownload.isEmpty()) {
//                return DownloadResult.empty();
//            }
//
//            List<Path> paths = this.downloadBills(billsToDownload, cookies);
//
//            EnercoopBill latestBill = billsToDownload.stream()
//                    .max(Comparator.comparing(EnercoopBill::getDate))
//                    .orElseThrow();
//
//            return new DownloadResult(paths, latestBill.getDate().atStartOfDay());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        return DownloadResult.empty();
    }

//    private List<Path> downloadBills(List<EnercoopBill> bills, Map<String, String> cookies) throws IOException {
//        Path downloadDir = Paths.get(filatureConfiguration.tempDownloadPath());
//        if (!Files.exists(downloadDir)) {
//            Files.createDirectories(downloadDir);
//        }
//
//        var paths = new ArrayList<Path>();
//        for (EnercoopBill bill : bills) {
//            paths.add(downloadBill(bill, cookies));
//        }
//        return paths;
//    }
//
//    private Path downloadBill(EnercoopBill bill, Map<String, String> cookies) throws IOException {
//        String fileUrl = bill.getFileurl();
//        String filename = bill.getFilename();
//        Path filePath = Paths.get(filatureConfiguration.tempDownloadPath(), filename);
//        boolean fileExists = Files.exists(filePath);
//
//        if (fileExists) {
//            System.out.println("File already exists: " + filePath);
//        } else {
//            Connection.Response response = Jsoup.connect(fileUrl)
//                    .cookies(cookies)
//                    .ignoreContentType(true)
//                    .execute();
//
//            try (InputStream in = response.bodyStream();
//                 FileOutputStream out = new FileOutputStream(filePath.toFile())) {
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = in.read(buffer)) != -1) {
//                    out.write(buffer, 0, bytesRead);
//                }
//            }
//
//            Log.info("Downloaded bill: " + filePath);
//        }
//
//        return filePath;
//    }
//
//    private Map<String, String> authenticate(String login, String password) throws IOException {
//        Connection.Response loginPageResponse = Jsoup.connect(LOGIN_URL).execute();
//        Map<String, String> cookies = loginPageResponse.cookies();
//
//        Document loginPage = loginPageResponse.parse();
//        String hiddenToken = loginPage.select("#new_ecppp_client > input[name=\"authenticity_token\"]").attr("value");
//
//        Connection.Response loginResponse = Jsoup.connect(LOGIN_URL)
//                .data("authenticity_token", hiddenToken)
//                .data("ecppp_client[email]", login)
//                .data("ecppp_client[password]", password)
//                .data("ecppp_client[remember_me]", "true")
//                .data("commit", "Se+connecter")
//                .followRedirects(false)
//                .cookies(cookies)
//                .method(Connection.Method.POST)
//                .execute();
//
//        if (loginResponse.statusCode() != 302) {
//            throw new IOException("Login failed");
//        }
//
//        return loginResponse.cookies();
//    }
//
//    private List<EnercoopBill> fetchBills(Map<String, String> cookies) throws IOException {
//        List<EnercoopBill> bills = new ArrayList<>();
//
//        Document billPage = Jsoup.connect(BILL_URL)
//                .cookies(cookies)
//                .get();
//
//        Elements yearsLinks = billPage.select("div[class=\"dropdown dropdown-top\"] a");
//
//        for (Element yearLink : yearsLinks) {
//            String yearHref = yearLink.attr("href");
//            Document yearPage = Jsoup.connect("https://mon-espace.enercoop.fr" + yearHref)
//                    .cookies(cookies)
//                    .get();
//
//            Elements billsPerYear = yearPage.select("div[class=\"table-line table-line-collapse js-accordion-trigger\"] > div[class=\"row middle-xs\"]");
//
//            for (Element billInfo : billsPerYear) {
//                String text = billInfo.text();
//                Pattern pattern = Pattern.compile("([0-9]{4}) - (janvier|février|mars|avril|mai|juin|juillet|août|septembre|octobre|novembre|décembre) Réf. Facture ([A-Z0-9-]*) Total TTC ([0-9,]*)");
//                Matcher matcher = pattern.matcher(text);
//
//                if (matcher.find()) {
//                    String year = matcher.group(1);
//                    String monthStr = matcher.group(2);
//                    String vendorRef = matcher.group(3);
//                    String amountStr = matcher.group(4).replace(",", ".");
//
//                    int monthValue = getMonthValue(monthStr);
//                    LocalDate date = LocalDate.of(Integer.parseInt(year), monthValue, 1);
//                    double amount = Double.parseDouble(amountStr);
//
//                    EnercoopBill bill = new EnercoopBill();
//                    bill.setAmount(amount);
//                    bill.setCurrency("€");
//                    bill.setDate(date);
//                    bill.setVendor("Enercoop");
//                    bill.setVendorRef(vendorRef);
//                    bill.setFilename(String.format("%04d%02d_enercoop.pdf", date.getYear(), date.getMonthValue()));
//                    bill.setFileurl("https://mon-espace.enercoop.fr/invoice/" + vendorRef + "/pdf?invoice_type=factures");
//
//                    Map<String, Object> fileAttributes = new HashMap<>();
//                    Map<String, Object> metadata = new HashMap<>();
//                    metadata.put("contentAuthor", "enercoop.fr");
//                    metadata.put("issueDate", date);
//                    metadata.put("datetime", LocalDate.now());
//                    metadata.put("datetimeLabel", "issueDate");
//                    metadata.put("isSubscription", true);
//                    metadata.put("carbonCopy", true);
//                    metadata.put("qualification", "energy_invoice");
//                    fileAttributes.put("metadata", metadata);
//
//                    bill.setFileAttributes(fileAttributes);
//                    bills.add(bill);
//                }
//            }
//        }
//
//        return bills;
//    }

    private int getMonthValue(String monthName) {
        for (Month month : Month.values()) {
            if (month.getDisplayName(TextStyle.FULL, LOCALE_FR).equalsIgnoreCase(monthName)) {
                return month.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid month name: " + monthName);
    }
}
