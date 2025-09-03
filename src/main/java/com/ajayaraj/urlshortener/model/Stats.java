package com.ajayaraj.urlshortener.model;

import java.util.List;

public class Stats {

    private String longUrl;
    private String shortCode;
    private long hitCount;
    private List<UrlAccessLog> logs;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }

    public List<UrlAccessLog> getLogs() {
        return logs;
    }

    public void setLogs(List<UrlAccessLog> logs) {
        this.logs = logs;
    }
}
