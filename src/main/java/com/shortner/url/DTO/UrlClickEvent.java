package com.shortner.url.DTO;

public class UrlClickEvent {

    private String shortCode;
    private long timestamp;

    public UrlClickEvent() {}

    public UrlClickEvent(String shortCode, long timestamp) {
        this.shortCode = shortCode;
        this.timestamp = timestamp;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UrlClickEvent{" +
                "shortCode='" + shortCode + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}