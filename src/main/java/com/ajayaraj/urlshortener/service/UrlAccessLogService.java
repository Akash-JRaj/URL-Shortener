package com.ajayaraj.urlshortener.service;

import com.ajayaraj.urlshortener.model.UrlAccessLog;
import com.ajayaraj.urlshortener.repo.UrlAccessLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlAccessLogService {

    @Autowired
    private UrlAccessLogRepo urlAccessLogRepo;

    public List<UrlAccessLog> getByUrlStoreId(long id) {
        return urlAccessLogRepo.findByUrlStoreId(id);
    }

    public void addUrlAccessLog(UrlAccessLog urlAccessLog) {
        urlAccessLogRepo.save(urlAccessLog);
    }

}
