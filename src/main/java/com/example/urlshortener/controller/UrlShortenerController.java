package com.example.urlshortener.controller;

import com.example.urlshortener.model.OriginalUrlRequest;
import com.example.urlshortener.model.ShortUrlResponse;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.model.UrlHit;
import com.example.urlshortener.service.UrlHitService;
import com.example.urlshortener.service.UrlShorteningService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin("*")
public class UrlShortenerController {

    @Autowired
    private UrlShorteningService urlShorteningService;

    @Autowired
    private UrlHitService urlHitService;

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody List<OriginalUrlRequest> requests) {
        // Call the service method with the list of requests.
        List<ShortUrlResponse> responses = urlShorteningService.shortenUrls(requests);

        // Optionally, if there's only one response, return just that object instead of a list.
        if (responses.size() == 1) {
            return ResponseEntity.ok(responses.get(0));
        }
        return ResponseEntity.ok(responses);
    }
    /*@PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody List<OriginalUrlRequest> requests) {
        List<ShortUrlResponse> responses = urlShorteningService.shortenUrls(requests);
        return ResponseEntity.ok(responses);
    }*/

    @GetMapping("/{shortId}")
    public ResponseEntity<?> redirect(@PathVariable String shortId, HttpServletRequest request) {
        Optional<ShortenedUrl> optionalEntry = urlHitService.getOriginalUrl(shortId);

        if (optionalEntry.isPresent()) {
            ShortenedUrl urlEntry = optionalEntry.get();

            // Log the URL hit
            urlHitService.logUrlHit(urlEntry, request);

            return ResponseEntity.status(302)
                    .header("Location", urlEntry.getOriginalUrl())
                    .build();
        } else {
            return ResponseEntity.status(404).body("URL not found");
        }
    }

    @GetMapping("/stats/{shortId}")
    public ResponseEntity<?> getStats(@PathVariable String shortId) {
        List<UrlHit> hits = urlHitService.getUrlHits(shortId);
        return ResponseEntity.ok(hits);
    }
}