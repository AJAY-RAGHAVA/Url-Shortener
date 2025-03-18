package com.example.urlshortener.model;

public class OriginalUrlRequest {
    private String originalUrl;
    private String senderAccountNumber;
    private String cdsid;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public String getCdsid(){
        return cdsid;
    }
}
