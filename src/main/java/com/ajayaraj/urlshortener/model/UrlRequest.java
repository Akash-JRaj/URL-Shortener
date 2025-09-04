package com.ajayaraj.urlshortener.model;

import java.time.LocalDateTime;

public class UrlRequest {
    private String longUrl;
    private LocalDateTime expiresAt;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
