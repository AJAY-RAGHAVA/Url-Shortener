package com.example.urlshortener.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.urlshortener.model.OriginalUrlRequest;
import com.example.urlshortener.model.ShortUrlResponse;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UrlShorteningService {

    @Autowired
    private ShortenedUrlRepository shortenedUrlRepository;

    @Value("${url.shortener.base-url}")
    private String baseUrl;

    public List<ShortUrlResponse> shortenUrls(List<OriginalUrlRequest> requests) {
        List<ShortUrlResponse> responses = new ArrayList<>();

        for (OriginalUrlRequest request : requests) {
            String originalUrl = sanitizeInput(request.getOriginalUrl());
            int senderAccountNumber = request.getSenderAccountNumber();
            String cdsid = request.getCdsid();

            Optional<ShortenedUrl> existingEntry =
                    shortenedUrlRepository.findByOriginalUrlAndSenderAccountNumber(originalUrl, senderAccountNumber);

            String shortUrl;
            if (existingEntry.isPresent()) {
                shortUrl = existingEntry.get().getShortUrl();
            } else {
                String shortId = NanoIdUtils.randomNanoId(
                        NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
                        NanoIdUtils.DEFAULT_ALPHABET,
                        6);
                shortUrl = baseUrl + shortId;

                ShortenedUrl newEntry = new ShortenedUrl();
                newEntry.setShortUrl(shortUrl);
                newEntry.setOriginalUrl(originalUrl);
                newEntry.setCreatedDateTime(LocalDateTime.now());
                newEntry.setCdsid(cdsid);
                newEntry.setSenderAccountNumber(senderAccountNumber);

                shortenedUrlRepository.save(newEntry);
            }
            responses.add(new ShortUrlResponse(shortUrl));
        }
        return responses;
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("[\n\r\t]", "");
    }
}
