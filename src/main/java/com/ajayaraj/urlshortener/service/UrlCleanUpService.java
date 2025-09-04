package com.ajayaraj.urlshortener.service;

import com.ajayaraj.urlshortener.model.UrlStore;
import com.ajayaraj.urlshortener.repo.UrlAccessLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlCleanUpService {

    private final UrlShortenerService urlShortenerService;
    private final UrlAccessLogRepo urlAccessLogRepo;

    @Autowired
    public UrlCleanUpService(UrlShortenerService urlShortenerService, UrlAccessLogRepo urlAccessLogRepo) {
        this.urlShortenerService = urlShortenerService;
        this.urlAccessLogRepo = urlAccessLogRepo;
    }

    @Scheduled(fixedRate = 3600)
    public void cleanUpExpiredUrls() {
        List<UrlStore> allUrls = urlShortenerService.getExpiredUrls();

        for(UrlStore urlStore : allUrls) {
            urlAccessLogRepo.deleteAll(urlAccessLogRepo.findByUrlStoreId(urlStore.getId()));
            urlShortenerService.remove(urlStore);
        }
    }

}
