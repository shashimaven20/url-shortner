package com.shortner.url.service;

import com.shortner.url.repo.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Service
public class ClickBatchUpdater {

    private final StringRedisTemplate redisTemplate;
    private final UrlRepository urlRepository;

    private static final Logger log = LoggerFactory.getLogger(ClickBatchUpdater.class);

    public ClickBatchUpdater(StringRedisTemplate redisTemplate,
            UrlRepository urlRepository) {
        this.redisTemplate = redisTemplate;
        this.urlRepository = urlRepository;
    }

    @Scheduled(fixedRate = 15000)
    public void flushClicks() {

        log.info("Inside batch updater.");
        Map<Object, Object> clickCounts = redisTemplate.opsForHash().entries("url_clicks");
        Map<Object, Object> lastAccessed = redisTemplate.opsForHash().entries("url_last_accessed");

        for (Object shortCodeObj : clickCounts.keySet()) {

            String shortCode = shortCodeObj.toString();
            int count = Integer.parseInt(clickCounts.get(shortCode).toString());

            Long lastAccessMillis = Long.parseLong(
                    lastAccessed.getOrDefault(shortCode, "0").toString());

            LocalDateTime lastAccessTime =
                    Instant.ofEpochMilli(lastAccessMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
            urlRepository.updateAnalytics(shortCode, count, lastAccessTime);
        }

        redisTemplate.delete("url_clicks");
        redisTemplate.delete("url_last_accessed");
    }
}