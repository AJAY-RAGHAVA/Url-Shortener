package com.example.urlshortener.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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
}
