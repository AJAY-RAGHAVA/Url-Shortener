package com.example.urlshortener.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "SHORTENED_URLS", uniqueConstraints = @UniqueConstraint(columnNames = {"ORIGINAL_URL", "ACCOUNT_NUMBER"}))
public class ShortenedUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERIAL_ID")
    private Long primaryId;

    @Column(name = "SHORT_URL", nullable = false, length = 100, unique = true)
    private String shortUrl;

    @Column(name = "ORIGINAL_URL", columnDefinition = "TEXT", nullable = false)
    private String originalUrl;

    @Column(name = "LSTUPDT_TS", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "LSTUPDT_USER_ID_CD", length = 12, nullable = false)
    private String cdsid;

    @Column(name = "ACCOUNT_NUMBER", nullable = false)
    private int senderAccountNumber;

    public ShortenedUrl() {
    }

    public ShortenedUrl(long primaryId, String cdsid, String shortUrl, String originalUrl, LocalDateTime createdDateTime, int senderAccountNumber) {
        this.primaryId = primaryId;
        this.shortUrl = shortUrl;
        this.cdsid = cdsid;
        this.originalUrl = originalUrl;
        this.createdDateTime = createdDateTime;
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(int senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public void setCdsid(String cdsid) {
        this.cdsid = cdsid;
    }

    public String getCdsid() {
        return cdsid;
    }
}
