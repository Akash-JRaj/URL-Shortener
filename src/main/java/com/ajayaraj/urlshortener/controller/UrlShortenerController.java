package com.ajayaraj.urlshortener.controller;

import com.ajayaraj.urlshortener.model.UrlRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UrlShortenerController {

    private static final String BASE_URL = "http://localhost:8080/";

    Map<String, String> urlMap = new HashMap<>();

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody UrlRequest request) {

        String longUrl = request.getLongUrl();

        String shortUrl = UUID.randomUUID().toString().substring(0,6);

        urlMap.put(shortUrl, longUrl);

        return ResponseEntity.ok(BASE_URL + shortUrl);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> getUrl(@PathVariable String code) {

        if(!urlMap.containsKey(code)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlMap.get(code))).build();
    }

}
