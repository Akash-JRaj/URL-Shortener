package com.ajayaraj.urlshortener.service;

import com.ajayaraj.urlshortener.model.UrlStore;
import com.ajayaraj.urlshortener.repo.UrlShortenerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;

@Service
public class UrlShortenerService {

    @Autowired
    private UrlShortenerRepo urlShortenerRepo;

    public String generateCode() {
        return UUID.randomUUID().toString().substring(0,6);
    }

    public UrlStore getByShortCode(String shortCode) {
        return urlShortenerRepo.findByShortUrl(shortCode);
    }

    public UrlStore getByLongUrl(String longUrl) {
        return urlShortenerRepo.findByLongUrl(longUrl);
    }

    public void addUrlStore(UrlStore urlStore) {
        urlShortenerRepo.save(urlStore);
    }

    public boolean isValidUrl(String url) {
        try {
            URI parsed = new URI(url);

            if(parsed.getHost() == null || parsed.getScheme() == null) {
                return false;
            }

            String scheme = parsed.getScheme();

            if(!scheme.equals("http") && !scheme.equals("https")) {
                return false;
            }

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
