package com.shortner.url.repo;

import com.shortner.url.entity.UrlMapping;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortCode(String shortCode);
    Optional<UrlMapping> findByOriginalUrl(String originalUrl);

    @Modifying
    @Transactional
    @Query("UPDATE UrlMapping u SET u.clickCount = u.clickCount + 1, u.lastAccessed = CURRENT_TIMESTAMP WHERE u.shortCode = :shortCode")
    void incrementClick(@Param("shortCode") String shortCode);

    @Modifying
    @Transactional
    @Query("UPDATE UrlMapping u SET u.clickCount = u.clickCount + :count, u.lastAccessed = :lastAccessed WHERE u.shortCode = :shortCode")
    void updateAnalytics(@Param("shortCode") String shortCode,
            @Param("count") int count,
            @Param("lastAccessed") LocalDateTime lastAccessed);

}