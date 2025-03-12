package com.example.urlshortener.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;


@Entity
@Table(name = "url_hits")
public class UrlHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // optional, matches the column name in the database
    private Long id;

    @Column(name = "short_url", nullable = false)
    private String shortUrl;

    @Column(name = "time_of_hit", nullable = false)
    private LocalDateTime timeOfHit;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "browser_type")
    private String browserType;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    public UrlHit() {
    }

    public UrlHit(String shortUrl, LocalDateTime timeOfHit, String ipAddress, String browserType, String accountNumber) {
        this.shortUrl = shortUrl;
        this.timeOfHit = timeOfHit;
        this.ipAddress = ipAddress;
        this.browserType = browserType;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDateTime getTimeOfHit() {
        return timeOfHit;
    }

    public void setTimeOfHit(LocalDateTime timeOfHit) {
        this.timeOfHit = timeOfHit;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }
}
