package com.ajayaraj.urlshortener.controller;

import com.ajayaraj.urlshortener.model.Stats;
import com.ajayaraj.urlshortener.model.UrlAccessLog;
import com.ajayaraj.urlshortener.model.UrlRequest;
import com.ajayaraj.urlshortener.model.UrlStore;
import com.ajayaraj.urlshortener.service.UrlAccessLogService;
import com.ajayaraj.urlshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UrlShortenerController {

    private static final String BASE_URL = "http://localhost:8080/";

    @Autowired
    private UrlShortenerService urlShortenerService;

    @Autowired
    private UrlAccessLogService urlAccessLogService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody UrlRequest request) {

        String longUrl = request.getLongUrl();

        if(!urlShortenerService.isValidUrl(longUrl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid URL : " + longUrl);
        }

        UrlStore exists = urlShortenerService.getByLongUrl(longUrl);

        if(exists != null) {
            exists.setExpiresAt(request.getExpiresAt());
            urlShortenerService.updateUrlStore(exists);

            return ResponseEntity.ok(BASE_URL + exists.getShortUrl());
        }

        String shortCode = urlShortenerService.generateCode();

        UrlStore urlStore = new UrlStore();
        urlStore.setLongUrl(longUrl);
        urlStore.setShortUrl(shortCode);
        urlStore.setCreatedAt(LocalDateTime.now());
        urlStore.setExpiresAt(request.getExpiresAt());

        urlShortenerService.addUrlStore(urlStore);

        return ResponseEntity.ok(BASE_URL + shortCode);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> getUrl(@PathVariable String code, HttpServletRequest request) {

        UrlStore urlStore = urlShortenerService.getByShortCode(code);

        if(urlStore == null) {
            return ResponseEntity.notFound().build();
        }

        if(urlStore.getExpiresAt() != null && urlStore.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }

        urlStore.setHitCount(urlStore.getHitCount() + 1);
        urlShortenerService.addUrlStore(urlStore);

        UrlAccessLog urlAccessLog = new UrlAccessLog();

        urlAccessLog.setUrlStoreId(urlStore.getId());
        urlAccessLog.setIp(request.getRemoteAddr());

        urlAccessLogService.addUrlAccessLog(urlAccessLog);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlStore.getLongUrl())).build();
    }

    @GetMapping("/stats/{code}")
    public ResponseEntity<Stats> getStats(@PathVariable String code) {
        UrlStore urlStore = urlShortenerService.getByShortCode(code);

        if(urlStore == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<UrlAccessLog> logs = urlAccessLogService.getByUrlStoreId(urlStore.getId());

        Stats stats = new Stats();

        stats.setLongUrl(urlStore.getLongUrl());
        stats.setShortCode(urlStore.getShortUrl());
        stats.setHitCount(urlStore.getHitCount());
        stats.setExpired(urlStore.getExpiresAt() != null && urlStore.getExpiresAt().isBefore(LocalDateTime.now()));

        stats.setLogs(logs);

        return ResponseEntity.ok(stats);
    }

}
