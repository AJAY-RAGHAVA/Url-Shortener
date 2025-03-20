package com.example.urlshortener.service;

import com.example.urlshortener.entity.ShortenedUrl;
import com.example.urlshortener.entity.UrlHit;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import com.example.urlshortener.repository.UrlHitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UrlHitService {

    private final ShortenedUrlRepository shortenedUrlRepository;

    private final UrlHitRepository urlHitRepository;

    @Value("${url.shortener.base-url}")
    private String baseUrl;

    public UrlHitService(ShortenedUrlRepository shortenedUrlRepository, UrlHitRepository urlHitRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
        this.urlHitRepository = urlHitRepository;
    }

    public Optional<ShortenedUrl> getOriginalUrl(String shortId) {
        String shortUrl = baseUrl + shortId;
        return shortenedUrlRepository.findByShortUrl(shortUrl);
    }

    public void logUrlHit(ShortenedUrl urlEntry, HttpServletRequest request) {
        UrlHit hit = new UrlHit();
        hit.setShortUrl(urlEntry.getShortUrl());
        hit.setTimeOfHit(LocalDateTime.now());
        hit.setIpAddress(getClientIpAddress(request));
        hit.setBrowserType(getBrowserTypeFromUserAgent(request.getHeader("User-Agent")));
        hit.setAccountNumber(urlEntry.getSenderAccountNumber());
        hit.setCdsid(urlEntry.getCdsid());

        urlHitRepository.save(hit);
    }

    public List<UrlHit> getUrlHits(String shortId) {
        String shortUrl = baseUrl + shortId;
        return urlHitRepository.findByShortUrl(shortUrl);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        } else {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress.trim();
    }

    private String getBrowserTypeFromUserAgent(String userAgent) {
        if (userAgent == null) return "Unknown";
        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("chrome")) {
            return "Chrome";
        } else if (userAgent.contains("firefox")) {
            return "Firefox";
        } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
            return "Safari";
        } else if (userAgent.contains("edge")) {
            return "Edge";
        } else if (userAgent.contains("msie") || userAgent.contains("trident")) {
            return "Internet Explorer";
        } else {
            return "Other";
        }
    }
}