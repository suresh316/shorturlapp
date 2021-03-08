package com.assignment.url.dao;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ShortUrl datasource or repository
 */
public interface ShortUrlDataSource extends JpaRepository<ShortUrl, String> {
}
