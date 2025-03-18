package com.example.urlshortener.model;

public class OriginalUrlRequest {
    private String originalUrl;
    private int senderAccountNumber;
    private String cdsid;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public int getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public String getCdsid(){
        return cdsid;
    }
}
