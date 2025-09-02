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

        UrlStore longUrl = urlShortenerService.getByShortCode(code);

        if(longUrl == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl.getLongUrl())).build();
    }

}
