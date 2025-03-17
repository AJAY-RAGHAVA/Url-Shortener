package com.example.urlshortener.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.urlshortener.model.BulkShortUrlResponse;
import com.example.urlshortener.model.OriginalUrlRequest;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.UrlHit;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import com.example.urlshortener.repository.UrlHitRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// Removed unused imports
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            String originalUrl = sanitizeInput(request.getOriginalUrl());
            String senderAccountNumber = sanitizeInput(request.getSenderAccountNumber());

            // Check if a matching ShortenedUrl already exists
            Optional<ShortenedUrl> existingEntry = shortenedUrlRepository.findByOriginalUrlAndSenderAccountNumber(originalUrl, senderAccountNumber);

            String shortId;

            if (existingEntry.isPresent()) {
                // Use existing short ID
                shortId = existingEntry.get().getShortId();
            } else {
                // Generate new short ID
                shortId = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 6);

                // Create and save new ShortenedUrl
                ShortenedUrl newEntry = new ShortenedUrl();
                newEntry.setShortId(shortId);
                newEntry.setOriginalUrl(originalUrl);
                newEntry.setCreatedDateTime(LocalDateTime.now());
                newEntry.setSenderAccountNumber(senderAccountNumber);

                shortenedUrlRepository.save(newEntry);
            }

            String shortUrl = BASE_URL + shortId;

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
    public ResponseEntity<?> redirect(@PathVariable String shortId, HttpServletRequest request) {
        logger.info("Received request for shortId: {}", shortId);

        Optional<ShortenedUrl> optionalEntry = shortenedUrlRepository.findByShortId(shortId);

        if (optionalEntry.isPresent()) {
            logger.info("ShortenedUrl entry found for shortId: {}", shortId);
            ShortenedUrl urlEntry = optionalEntry.get();

            // Create UrlHit entry
            UrlHit hit = new UrlHit();
            hit.setShortUrl(BASE_URL + shortId);
            hit.setTimeOfHit(LocalDateTime.now());
            hit.setIpAddress(getClientIpAddress(request));
            hit.setBrowserType(getBrowserTypeFromUserAgent(request.getHeader("User-Agent")));
            hit.setAccountNumber(urlEntry.getSenderAccountNumber());

            // Log the hit details
            logger.info("Recording UrlHit: {}", hit);

            urlHitRepository.save(hit);

            return ResponseEntity.status(302)
                    .header("Location", urlEntry.getOriginalUrl())
                    .build();
        } else {
            logger.warn("No ShortenedUrl entry found for shortId: {}", shortId);
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
}
