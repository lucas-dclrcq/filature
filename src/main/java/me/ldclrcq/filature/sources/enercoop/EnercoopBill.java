package me.ldclrcq.filature.sources.enercoop;

import java.time.LocalDate;
import java.util.Map;

public class EnercoopBill {
    private double amount;
    private String currency;
    private LocalDate date;
    private String vendor;
    private String vendorRef;
    private String filename;
    private String fileurl;
    private Map<String, Object> fileAttributes;

    public EnercoopBill() {
    }

    public EnercoopBill(double amount, String currency, LocalDate date, String vendor, String vendorRef, String filename, String fileurl, Map<String, Object> fileAttributes) {
        this.amount = amount;
        this.currency = currency;
        this.date = date;
        this.vendor = vendor;
        this.vendorRef = vendorRef;
        this.filename = filename;
        this.fileurl = fileurl;
        this.fileAttributes = fileAttributes;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getVendorRef() {
        return vendorRef;
    }

    public void setVendorRef(String vendorRef) {
        this.vendorRef = vendorRef;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public Map<String, Object> getFileAttributes() {
        return fileAttributes;
    }

    public void setFileAttributes(Map<String, Object> fileAttributes) {
        this.fileAttributes = fileAttributes;
    }
}