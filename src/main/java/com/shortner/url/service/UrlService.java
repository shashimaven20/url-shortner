package com.shortner.url.service;

import com.shortner.url.entity.UrlMapping;
import com.shortner.url.repo.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public UrlMapping createShortUrl(String originalUrl) {
        String shortCode = generateShortCode();
        UrlMapping url = new UrlMapping();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        return urlRepository.save(url);
    }

    public UrlMapping checkOriginalUrlExists(String originalUrl) {
        Optional<UrlMapping> existing = urlRepository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return existing.get();
        }
        return createShortUrl(originalUrl);
    }
    public String getOriginalUrl(String shortCode) {
        UrlMapping url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return url.getOriginalUrl();
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}