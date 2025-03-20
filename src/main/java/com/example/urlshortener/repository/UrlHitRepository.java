package com.example.urlshortener.repository;

import com.example.urlshortener.entity.UrlHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlHitRepository extends JpaRepository<UrlHit, Long> {
    List<UrlHit> findByShortUrl(String shortUrl);
}
