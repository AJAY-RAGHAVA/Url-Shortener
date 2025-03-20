package com.example.urlshortener.model;

import lombok.Data;

@Data
public class ShortUrlResponse {
    private String shortUrl;

    public ShortUrlResponse(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
