package com.example.urlshortener.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shortened_urls")
public class ShortenedUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_id", nullable = false, unique = true)
    private String shortId;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    // Constructors
    public ShortenedUrl() {
    }

    public ShortenedUrl(long primaryId, String getShortId, String originalUrl, LocalDateTime createdDateTime, String senderAccountNumber) {
        this.id = primaryId;
        this.shortId = getShortId;
        this.originalUrl = originalUrl;
        this.createdDateTime = createdDateTime;
        this.senderAccountNumber = senderAccountNumber;
    }

    // Getters and Setters
    public long getPrimaryId() {
        return id;
    }

    public void setPrimaryId(long primaryId) {
        this.id = primaryId;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortUrl) {
        this.shortId = shortUrl;
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

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }
}
