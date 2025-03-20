package com.example.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "TRACING_SHORTENED_URLS", uniqueConstraints = @UniqueConstraint(columnNames = {"ORIGINAL_URL", "ACCOUNT_NUMBER"}))
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
}
