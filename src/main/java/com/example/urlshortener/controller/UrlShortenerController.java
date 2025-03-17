package com.example.urlshortener.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.urlshortener.model.BulkShortUrlResponse;
import com.example.urlshortener.model.OriginalUrlRequest;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.UrlHit;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import com.example.urlshortener.repository.UrlHitRepository;
import com.google.api.gax.rpc.internal.Headers;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class UrlShortenerController {

    private static final String BASE_URL = "https://url-shortener-3vc2dqspzq-el.a.run.app/"; // Your domain name

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

    @Autowired
    private ShortenedUrlRepository shortenedUrlRepository;

    @Autowired
    private UrlHitRepository urlHitRepository;

    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[\n\r\t]", "");
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody List<OriginalUrlRequest> requests) {
        List<BulkShortUrlResponse> responses = new ArrayList<>();

        for (OriginalUrlRequest request : requests) {
            String originalUrl = request.getOriginalUrl();
            String senderAccountNumber = sanitizeInput(request.getSenderAccountNumber());

            // Check if a matching ShortenedUrl already exists
            Optional<ShortenedUrl> existingEntry = shortenedUrlRepository.findByOriginalUrlAndSenderAccountNumber(originalUrl, senderAccountNumber);

            String shortUrl;

            if (existingEntry.isPresent()) {
                // Use existing short URL
                shortUrl = existingEntry.get().getShortUrl();
            } else {
                // Generate new short ID
                String shortId = sanitizeInput(NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 6));
                shortUrl = BASE_URL + shortId;

                // Create and save new ShortenedUrl
                ShortenedUrl newEntry = new ShortenedUrl();
                newEntry.setShortUrl(shortUrl);
                newEntry.setOriginalUrl(originalUrl);
                newEntry.setCreatedDateTime(LocalDateTime.now());
                newEntry.setSenderAccountNumber(senderAccountNumber);

                shortenedUrlRepository.save(newEntry);
            }

            // Create response item
            BulkShortUrlResponse responseItem = new BulkShortUrlResponse();
            responseItem.setOriginalUrl(originalUrl);
            responseItem.setSenderAccountNumber(senderAccountNumber);
            responseItem.setShortUrl(shortUrl);

            responses.add(responseItem);
        }

        return ResponseEntity.ok(responses.size() == 1 ? responses.get(0) : responses);
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<?> redirect(@RequestHeader Headers headers, @PathVariable String shortId, HttpServletRequest request) {
        String shortUrl = BASE_URL + shortId;

        Optional<ShortenedUrl> optionalEntry = shortenedUrlRepository.findByShortUrl(shortUrl);

        logger.info("The headers are: {}", headers);
        logRequestHeaders(request);

        if (optionalEntry.isPresent()) {
            ShortenedUrl urlEntry = optionalEntry.get();

            // Create UrlHit entry
            UrlHit hit = new UrlHit();
            hit.setShortUrl(shortUrl);
            hit.setTimeOfHit(LocalDateTime.now());
            hit.setIpAddress(getClientIpAddress(request));
            hit.setBrowserType(getBrowserTypeFromUserAgent(request.getHeader("User-Agent")));
            hit.setAccountNumber(urlEntry.getSenderAccountNumber());

            urlHitRepository.save(hit);

            return ResponseEntity.status(302)
                    .header("Location", urlEntry.getOriginalUrl())
                    .build();
        } else {
            return ResponseEntity.status(404).body("URL not found");
        }
    }

    @GetMapping("/stats/{shortId}")
    public ResponseEntity<?> getStats(@PathVariable String shortId) {
        String shortUrl = BASE_URL + shortId;
        List<UrlHit> hits = urlHitRepository.findByShortUrl(shortUrl);
        return ResponseEntity.ok(hits);
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
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari") && !userAgent.contains("Chrome")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "Internet Explorer";
        } else {
            return "Other";
        }
    }

    private void logRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("{}: {}", headerName, headerValue);
        }
    }
}
