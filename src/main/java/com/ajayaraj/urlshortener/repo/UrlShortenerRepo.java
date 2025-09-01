package com.ajayaraj.urlshortener.repo;

import com.ajayaraj.urlshortener.model.UrlStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortenerRepo extends JpaRepository<UrlStore, String> {
    UrlStore findByShortUrl(String code);
}
