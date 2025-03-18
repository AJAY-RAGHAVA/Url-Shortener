package com.example.urlshortener.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;


@Entity
@Table(name = "SHORTURL_TRACKING_AUDIT")
public class UrlHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERIAL_ID")
    private Long id;

    @Column(name = "SHORT_URL", nullable = false, length = 100)
    private String shortUrl;

    @Column(name = "LSTUPDT_TS", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime timeOfHit;

    @Column(name = "LSTUPDT_USER_ID_CD", nullable = false, length = 12)
    private String cdsid;

    @Column(name = "IP_ADDRESS", length = 20,nullable = false)
    private String ipAddress;

    @Column(name = "BROWSER_TYPE", length = 20)
    private String browserType;

    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    private int accountNumber;

    public UrlHit() {
    }

    public UrlHit(String shortUrl, String cdsid, LocalDateTime timeOfHit, String ipAddress, String browserType, int accountNumber) {
        this.shortUrl = shortUrl;
        this.cdsid = cdsid;
        this.timeOfHit = timeOfHit;
        this.ipAddress = ipAddress;
        this.browserType = browserType;
        this.accountNumber = accountNumber;
    }

    // Getters and Setters

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getCdsid(){
        return cdsid;
    }

    public void setCdsid(String cdsid){
        this.cdsid = cdsid;
    }

    public void setTimeOfHit(LocalDateTime timeOfHit) {
        this.timeOfHit = timeOfHit;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }
}
