package com.example.urlshortener.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.urlshortener.model.*;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import com.example.urlshortener.repository.UrlHitRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class UrlShortenerController {

    private static final String BASE_URL = "https://url-shortener-3vc2dqspzq-el.a.run.app/";
    // private static final String BASE_URL = "http://localhost:8080/";"

    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerController.class);

//    private static final String BASE_URL = "http://your-domain.com/"; //Ensure you replace "http://your-domain.com/" with your actual domain or base URL.

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
    public ResponseEntity<?> shortenUrl(@RequestBody OriginalUrlRequest request, HttpServletRequest requests) {

        String originalUrl = request.getOriginalUrl();
        String senderAccountNumber = request.getSenderAccountNumber();

        // Check if a matching ShortenedUrl already exists
        Optional<ShortenedUrl> existingEntry = shortenedUrlRepository.findByOriginalUrlAndSenderAccountNumber(originalUrl, senderAccountNumber);

        if (existingEntry.isPresent()) {
            // Return existing short URL
            return ResponseEntity.ok(new ShortUrlResponse(existingEntry.get().getShortUrl()));
        }

        // Generate new short ID
        String shortId = sanitizeInput(NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 6));
        String shortUrl = BASE_URL + shortId;

        // Create and save new ShortenedUrl
        ShortenedUrl newEntry = new ShortenedUrl();
        newEntry.setShortUrl(shortUrl);
        newEntry.setOriginalUrl(originalUrl);
        newEntry.setCreatedDateTime(LocalDateTime.now());
        newEntry.setSenderAccountNumber(senderAccountNumber);

        shortenedUrlRepository.save(newEntry);

        return ResponseEntity.ok(new ShortUrlResponse(shortUrl));
    }

    /*@PostMapping("/shorten/bulk")
    public ResponseEntity<?> bulkShortenUrl(@RequestBody List<OriginalUrlRequest> requests, HttpServletRequest servletRequest) {
        List<BulkShortUrlResponse> responses = new ArrayList<>();

        String baseUrl = servletRequest.getScheme() + "://" + servletRequest.getServerName();

        int serverPort = servletRequest.getServerPort();
        if (serverPort != 80 && serverPort != 443) {
            baseUrl += ":" + serverPort;
        }
        baseUrl += "/";

        for (OriginalUrlRequest request : requests) {
            BulkShortUrlResponse responseItem = new BulkShortUrlResponse();
            responseItem.setOriginalUrl(request.getOriginalUrl());
            responseItem.setSenderAccountNumber(request.getSenderAccountNumber());

            try {
                String originalUrl = request.getOriginalUrl();
                String senderAccountNumber = request.getSenderAccountNumber();

                // Validate the original URL and sender account number
                if (originalUrl == null || originalUrl.isEmpty() || senderAccountNumber == null || senderAccountNumber.isEmpty()) {
                    throw new IllegalArgumentException("Original URL and sender account number cannot be blank");
                }

                // Check if a matching ShortenedUrl already exists
                Optional<ShortenedUrl> existingEntry = shortenedUrlRepository.findByOriginalUrlAndSenderAccountNumber(originalUrl, senderAccountNumber);

                String shortUrl;

                if (existingEntry.isPresent()) {
                    // Use existing short URL
                    shortUrl = existingEntry.get().getShortUrl();
                } else {
                    // Generate new short ID
                    String shortId = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 6);
                    shortUrl = baseUrl + shortId;

                    // Create and save new ShortenedUrl
                    ShortenedUrl newEntry = new ShortenedUrl();
                    newEntry.setShortUrl(shortUrl);
                    newEntry.setOriginalUrl(originalUrl);
                    newEntry.setCreatedDateTime(LocalDateTime.now());
                    newEntry.setSenderAccountNumber(senderAccountNumber);

                    shortenedUrlRepository.save(newEntry);
                }

                responseItem.setShortUrl(shortUrl);
            } catch (Exception e) {
                responseItem.setShortUrl(null);
                throw new IllegalArgumentException(e.getMessage());
            }

            responses.add(responseItem);
        }

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody List<OriginalUrlRequest> requests, HttpServletRequest servletRequest) {
        List<BulkShortUrlResponse> responses = new ArrayList<>();

        for (OriginalUrlRequest request : requests) {
            String originalUrl = request.getOriginalUrl();
            String senderAccountNumber = request.getSenderAccountNumber();

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
    }*/

    @GetMapping("/{shortId}")
    public ResponseEntity<?> redirect(@RequestHeader Header headers, @PathVariable String shortId, HttpServletRequest request) {
        String shortUrl = BASE_URL + shortId;

        Optional<ShortenedUrl> optionalEntry = shortenedUrlRepository.findByShortUrl(shortUrl);

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
            logger.info("The Header values are: \n",request);

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

    private String getLocationFromIp(String ipAddress) {
        String apiUrl = "https://ip-api.com/json/" + ipAddress;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);
            // Parse JSON and extract location fields
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            String status = root.path("status").asText();
            if ("success".equalsIgnoreCase(status)) {
                String city = root.path("city").asText("");
                String country = root.path("country").asText("");
                String regionName = root.path("regionName").asText("");
                return city + ", " + regionName + ", " + country;
            } else {
                String message = root.path("message").asText("Unknown error");
                System.err.println("Geolocation API error: " + message);
                return "Unknown";
            }
        } catch (Exception e) {
            System.err.println("Error in getLocationFromIp: " + e.getMessage());
            return "Unknown";
        }
    }


}
