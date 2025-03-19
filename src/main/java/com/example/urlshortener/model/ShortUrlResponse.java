package com.example.urlshortener.model;

public class ShortUrlResponse {
    private String shortUrl;
    private String originalUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public ShortUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
