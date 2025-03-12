package com.example.urlshortener.model;

public class ShortUrlResponse {
    private String shortUrl;

    public ShortUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    // Getter
    public String getShortUrl() {
        return shortUrl;
    }
}
