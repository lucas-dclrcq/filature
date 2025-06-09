package me.ldclrcq.filature.sources.connectors.free;

import java.time.LocalDate;
import java.util.Map;

public class FreeBill {
    private double amount;
    private LocalDate date;
    private String vendor;
    private String vendorRef;
    private String filename;
    private String fileurl;
    private String contractReference;
    private Map<String, Object> fileAttributes;

    public FreeBill() {
    }

    public FreeBill(double amount, LocalDate date, String vendor, String vendorRef, String filename, String fileurl, String contractReference, Map<String, Object> fileAttributes) {
        this.amount = amount;
        this.date = date;
        this.vendor = vendor;
        this.vendorRef = vendorRef;
        this.filename = filename;
        this.fileurl = fileurl;
        this.contractReference = contractReference;
        this.fileAttributes = fileAttributes;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

    public String getContractReference() {
        return contractReference;
    }

    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    public Map<String, Object> getFileAttributes() {
        return fileAttributes;
    }

    public void setFileAttributes(Map<String, Object> fileAttributes) {
        this.fileAttributes = fileAttributes;
    }
}