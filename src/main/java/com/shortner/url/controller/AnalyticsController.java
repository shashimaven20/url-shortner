package com.shortner.url.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/top-urls")
    public Set<ZSetOperations.TypedTuple<Object>> getTopUrls() {

        return redisTemplate.opsForZSet()
                .reverseRangeWithScores("popular_urls", 0, 4);
    }

    @GetMapping("/clicks/{shortCode}")
    public Object getClicks(@PathVariable String shortCode) {

        return redisTemplate.opsForHash()
                .get("url_clicks", shortCode);
    }

    @GetMapping("/total-clicks")
    public Long getTotalClicks() {

        Map<Object,Object> clicks =
                redisTemplate.opsForHash().entries("url_clicks");

        return clicks.values()
                .stream()
                .mapToLong(v -> Long.parseLong(v.toString()))
                .sum();
    }
}