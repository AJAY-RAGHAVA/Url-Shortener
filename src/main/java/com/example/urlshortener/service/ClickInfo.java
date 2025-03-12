package com.example.urlshortener.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ClickInfo {
    private String ip;
    private String userAgent;
    private String referer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Constructors
    public ClickInfo() {
    }

    public ClickInfo(String ip, String userAgent, String referer, LocalDateTime timestamp) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.referer = referer;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getUserAgent() {
        return userAgent;
    }
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
