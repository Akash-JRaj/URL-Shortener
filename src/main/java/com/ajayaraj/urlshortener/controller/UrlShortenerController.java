package com.ajayaraj.urlshortener.controller;

import com.ajayaraj.urlshortener.model.UrlRequest;
import com.ajayaraj.urlshortener.model.UrlStore;
import com.ajayaraj.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlShortenerController {

    private static final String BASE_URL = "http://localhost:8080/";

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody UrlRequest request) {

        String longUrl = request.getLongUrl();

        if(!urlShortenerService.isValidUrl(longUrl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid URL : " + longUrl);
        }

        UrlStore exists = urlShortenerService.getByLongUrl(longUrl);

        if(exists != null) {
            return ResponseEntity.ok(BASE_URL + exists.getShortUrl());
        }

        String shortCode = urlShortenerService.generateCode();

        UrlStore urlStore = new UrlStore();
        urlStore.setLongUrl(longUrl);
        urlStore.setShortUrl(shortCode);

        urlShortenerService.addUrlStore(urlStore);

        return ResponseEntity.ok(BASE_URL + shortCode);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> getUrl(@PathVariable String code) {

        UrlStore urlStore = urlShortenerService.getByShortCode(code);

        if(urlStore == null) {
            return ResponseEntity.notFound().build();
        }

        urlStore.setHitCount(urlStore.getHitCount() + 1);
        urlShortenerService.addUrlStore(urlStore);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlStore.getLongUrl())).build();
    }

    @GetMapping("/stats/{code}")
    public ResponseEntity<UrlStore> getStats(@PathVariable String code) {
        UrlStore urlStore = urlShortenerService.getByShortCode(code);

        if(urlStore == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(urlStore);
    }

}
