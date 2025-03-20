package com.example.urlshortener.repository;

import com.example.urlshortener.entity.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByShortUrl(String shortUrl);

    Optional<ShortenedUrl> findByOriginalUrlAndSenderAccountNumber(String originalUrl, int senderAccountNumber);
}

