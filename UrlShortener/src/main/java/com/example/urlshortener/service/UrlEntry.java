package com.example.urlshortener.service;

import java.util.ArrayList;
import java.util.List;

public class UrlEntry {
    private String shortId;
    private String originalUrl;
    private List<ClickInfo> clicks;

    // Constructors
    public UrlEntry() {
        this.clicks = new ArrayList<>();
    }

    public UrlEntry(String shortId, String originalUrl) {
        this.shortId = shortId;
        this.originalUrl = originalUrl;
        this.clicks = new ArrayList<>();
    }

    // Getters and Setters
    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public List<ClickInfo> getClicks() {
        return clicks;
    }

    public void setClicks(List<ClickInfo> clicks) {
        this.clicks = clicks;
    }
}
