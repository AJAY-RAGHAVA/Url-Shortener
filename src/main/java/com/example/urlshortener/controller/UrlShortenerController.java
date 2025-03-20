package com.example.urlshortener.controller;

import com.example.urlshortener.model.OriginalUrlRequest;
import com.example.urlshortener.model.ShortUrlResponse;
import com.example.urlshortener.entity.ShortenedUrl;
import com.example.urlshortener.entity.UrlHit;
import com.example.urlshortener.service.UrlHitService;
import com.example.urlshortener.service.UrlShorteningService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin("*")
public class UrlShortenerController {

    private final UrlShorteningService urlShorteningService;

    private final UrlHitService urlHitService;

    public UrlShortenerController(UrlShorteningService urlShorteningService, UrlHitService urlHitService) {
        this.urlShorteningService = urlShorteningService;
        this.urlHitService = urlHitService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<List<ShortUrlResponse>> shortenUrl(@RequestBody List<OriginalUrlRequest> requests) {
        List<ShortUrlResponse> responses = urlShorteningService.shortenUrls(requests);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<String> redirect(@PathVariable String shortId, HttpServletRequest request) {
        Optional<ShortenedUrl> optionalEntry = urlHitService.getOriginalUrl(shortId);
        if (optionalEntry.isPresent()) {
            ShortenedUrl urlEntry = optionalEntry.get();
            // Log URL hit
            urlHitService.logUrlHit(urlEntry, request);
            return ResponseEntity.status(302)
                    .header("Location", urlEntry.getOriginalUrl())
                    .build();
        } else {
            return ResponseEntity.status(404).body("URL not found");
        }
    }

    @GetMapping("/stats/{shortId}")
    public ResponseEntity<List<UrlHit>> getStats(@PathVariable String shortId) {
        List<UrlHit> hits = urlHitService.getUrlHits(shortId);
        return ResponseEntity.ok(hits);
    }
}
