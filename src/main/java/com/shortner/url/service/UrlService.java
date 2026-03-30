package com.shortner.url.service;

import com.shortner.url.entity.UrlMapping;
import com.shortner.url.repo.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    private ClickEventProducer clickEventProducer;

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Duration CACHE_TTL = Duration.ofHours(2);

    public UrlMapping createShortUrl(String originalUrl) {

        UrlMapping url = new UrlMapping();
        url.setOriginalUrl(originalUrl);
        UrlMapping saved = urlRepository.saveAndFlush(url);

        String shortCode = encodeBase62(saved.getId());
        saved.setShortCode(shortCode);

        //redisTemplate.opsForValue().set(shortCode, originalUrl, CACHE_TTL);

        return urlRepository.save(saved);
    }

    public UrlMapping checkOriginalUrlExists(String originalUrl) {
        Optional<UrlMapping> existing = urlRepository.findByOriginalUrl(originalUrl);
        if(existing.isPresent()) {
            UrlMapping url = existing.get();

            redisTemplate.opsForValue().set(url.getShortCode(), url.getOriginalUrl(), CACHE_TTL);

            return url;
        }
        return createShortUrl(originalUrl);
    }

    public String getOriginalUrl(String shortCode) {

        clickEventProducer.sendClickEvent(shortCode);

        String cached = redisTemplate.opsForValue().get(shortCode);
        if (cached != null) {
            return cached;
        }

        // fallback to DB if not cached
        UrlMapping url = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException("Not found"));

        // re‑cache the value
        redisTemplate.opsForValue().set(shortCode, url.getOriginalUrl(), CACHE_TTL);

        return url.getOriginalUrl();
    }

    public Set<String> getTopUrls() {
        return redisTemplate.opsForZSet().reverseRange("popular_urls",0,4); //top 5
    }

    private String generateShortCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(BASE62.charAt((int) (value % 62)));
            value /= 62;
        }

        return sb.reverse().toString();
    }
}