package com.shortner.url.service;

import com.shortner.url.DTO.UrlClickEvent;
import com.shortner.url.repo.UrlRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ClickEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ClickEventConsumer.class);

    @Autowired
    private UrlRepository urlRepository;

    private final StringRedisTemplate redisTemplate;
    @Autowired
    MeterRegistry meterRegistry;

    public ClickEventConsumer(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @KafkaListener(topics = "url-clicks")
    public void consume(UrlClickEvent event) {
        meterRegistry.counter("url_clicks_total",
                "shortCode", event.getShortCode()).increment();
        String shortCode = event.getShortCode();

        System.out.println("Event consumed: "+ event);
        log.info("event: {}", event);
        //urlRepository.incrementClick(event.getShortCode());

        redisTemplate.opsForHash().increment("url_clicks", shortCode, 1);
        redisTemplate.opsForHash().put("url_last_accessed", shortCode, String.valueOf(event.getTimestamp()));
        redisTemplate.opsForZSet().incrementScore("popular_urls", shortCode, 1);
    }
}