package com.example.urlshortener.service;


import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.urlshortener.model.OriginalUrlRequest;
import com.example.urlshortener.model.ShortUrlResponse;
import com.example.urlshortener.model.ShortenedUrl;
import com.example.urlshortener.repository.ShortenedUrlRepository;
import org.apache.logging.log4j.Logger;
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

//    private static final String BASE_URL = "https://url-shortener-3vc2dqspzq-el.a.run.app/";

    private static String BASE_URL;

    private Logger logger;

    @Autowired  // Constructor injection is recommended
    public UrlShorteningService(@Value("${base.url}") String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public List<ShortUrlResponse> shortenUrls(List<OriginalUrlRequest> requests) {
        List<ShortUrlResponse> responses = new ArrayList<>();

        logger.info("The base url is: {}", BASE_URL);

        for (OriginalUrlRequest request : requests) {
            String originalUrl = sanitizeInput(request.getOriginalUrl());
            String senderAccountNumber = request.getSenderAccountNumber();
            String cdsid = request.getCdsid();

            Optional<ShortenedUrl> existingEntry =
                    shortenedUrlRepository.findByOriginalUrlAndSenderAccountNumber(originalUrl, senderAccountNumber);

            String shortUrl;

            if (existingEntry.isPresent()) {
                shortUrl = existingEntry.get().getShortUrl();
            } else {
                String shortId = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 6);
                shortUrl = BASE_URL + shortId;

                ShortenedUrl newEntry = new ShortenedUrl();
                newEntry.setShortUrl(shortUrl);
                newEntry.setOriginalUrl(originalUrl);
                newEntry.setCreatedDateTime(LocalDateTime.now());
                newEntry.setCdsid(cdsid);
                newEntry.setSenderAccountNumber(Integer.parseInt(senderAccountNumber));

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

