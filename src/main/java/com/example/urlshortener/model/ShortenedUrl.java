package com.example.urlshortener.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "shortened_urls", uniqueConstraints = @UniqueConstraint(columnNames = {"original_url", "sender_account_number"}))
public class ShortenedUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "primary_id")
    private Long primaryId;

    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "original_url", columnDefinition = "TEXT", nullable = false)
    private String originalUrl;

    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime createdDateTime;

    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    // Constructors, getters, setters

    // Constructors
    public ShortenedUrl() {
    }

    public ShortenedUrl(long primaryId, String shortUrl, String originalUrl, LocalDateTime createdDateTime, String senderAccountNumber) {
        this.primaryId = primaryId;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.createdDateTime = createdDateTime;
        this.senderAccountNumber = senderAccountNumber;
    }

    // Getters and Setters
    public long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(long primaryId) {
        this.primaryId = primaryId;
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

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }
}
