package com.ajayaraj.urlshortener.repo;

import com.ajayaraj.urlshortener.model.UrlAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlAccessLogRepo extends JpaRepository<UrlAccessLog, Long> {
    List<UrlAccessLog> findByUrlStoreId(Long urlStoreId);
}
