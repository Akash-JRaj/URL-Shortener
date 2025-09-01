package com.ajayaraj.urlshortener.controller;

import com.ajayaraj.urlshortener.model.UrlRequest;
import com.ajayaraj.urlshortener.model.UrlStore;
import com.ajayaraj.urlshortener.repo.UrlShortenerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
public class UrlShortenerController {

    private static final String BASE_URL = "http://localhost:8080/";

    @Autowired
    private UrlShortenerRepo urlShortenerRepo;

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody UrlRequest request) {

        String longUrl = request.getLongUrl();

        String shortUrl = UUID.randomUUID().toString().substring(0,6);

        UrlStore urlStore = new UrlStore();
        urlStore.setLongUrl(longUrl);
        urlStore.setShortUrl(shortUrl);

        urlShortenerRepo.save(urlStore);

        return ResponseEntity.ok(BASE_URL + shortUrl);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> getUrl(@PathVariable String code) {

        UrlStore longUrl = urlShortenerRepo.findByShortUrl(code);

        if(longUrl == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl.getLongUrl())).build();
    }

}
