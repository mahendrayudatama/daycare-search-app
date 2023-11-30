package com.example.kidcare.Model;

public class ModelInvoice {
    String url;
    String invoiceDesc;
    String date;
    String daycareName;
    String invoiceID;

    public String getInvoiceID() {
        return invoiceID;
    }

    public String getDaycareName() {
        return daycareName;
    }


    public String getUrl() {
        return url;
    }

    public String getInvoiceDesc() {
        return invoiceDesc;
    }

    public String getDate() {
        return date;
    }

    public ModelInvoice(String url, String invoiceDesc, String date, String daycareName,String invoiceID) {
        this.url = url;
        this.invoiceDesc = invoiceDesc;
        this.date = date;
        this.daycareName = daycareName;
        this.invoiceID = invoiceID;
    }
}
