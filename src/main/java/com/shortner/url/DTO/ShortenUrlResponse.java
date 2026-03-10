package com.shortner.url.DTO;

public class ShortenUrlResponse {
    private final String shortUrl;

    public ShortenUrlResponse(String shortUrl) {
        this.shortUrl=shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
