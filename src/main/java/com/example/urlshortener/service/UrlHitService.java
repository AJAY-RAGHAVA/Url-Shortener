package com.example.urlshortener.service;


import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.UrlHit;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import com.example.urlshortener.repository.UrlHitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UrlHitService {

    @Autowired
    private ShortenedUrlRepository shortenedUrlRepository;

    @Autowired
    private UrlHitRepository urlHitRepository;

    private static final String BASE_URL = "https://url-shortener-3vc2dqspzq-el.a.run.app/";

    public Optional<ShortenedUrl> getOriginalUrl(String shortId) {
        String shortUrl = BASE_URL + shortId;
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
        String shortUrl = BASE_URL + shortId;
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
