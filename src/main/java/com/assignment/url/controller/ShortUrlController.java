package com.assignment.url.controller;

import com.assignment.url.dao.LongUrl;
import com.assignment.url.dao.ShortUrl;
import com.assignment.url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * This controller class offers various end points to create short url, get long url , other APIs etc
 */

@RestController
public class ShortUrlController {

    Logger logger = LogManager.getLogger(ShortUrlController.class);

    ShortUrl dummy = new ShortUrl();

    @Value("${server.domain}")
    private String serverDomain;

    @Autowired
    UrlService service;

    /**
     * This method creates short url for the given long url if already doesn't exists
     * in backend datasource
     *
     * @param url
     * @return short url
     */
    @PostMapping("/api/createShortUrl")
    String createShortUrl(@RequestBody LongUrl url) {
        logger.info("Received request for URL : {}", url);

        String shortUrl = service.createShortUrl(url).getShorUrl();

        logger.info("Short URL ID : {}  for URL : {}", shortUrl, url);

        return serverDomain + "/" + shortUrl;
    }

    /**
     * This method find the long url for the given short url
     *
     * @param shortUrl
     * @return long url
     */
    @GetMapping("/api/getUrl/{shortUrl}")
    String getFullUrl(@PathVariable String shortUrl) {
        return service.findUrl(shortUrl).orElseGet(() -> dummy).getUrl();
    }

    /**
     * This method find the short URLs for the give user
     * @param user
     * @return long url
     */
    @GetMapping("/api/getUrlByUser/{user}")
    List<ShortUrl> getUrlByUser(@PathVariable String user) {
        return service.getUrlByUser(user);
    }

    /**
     * This method returns the most used short URL
     *
     * @return long url
     */
    @GetMapping("/api/getMostUsedUrl")
    ShortUrl getMostUsedUrl() {
        return service.getMostUsedUrl();
    }

    /**
     * This method returns the most recently created short URL
     *
     * @return long url
     */
    @GetMapping("/api/getRecentCreatedUrl")
    ShortUrl getMostRecentCreatedUsedUrl() {
        return service.getMostRecentCreatedUrl();
    }

    /**
     * This is test method
     *
     * @return
     */
    @GetMapping("/")
    String getDefault() {
        return "<h1>Hello !! this is short url App <h1>";
    }

    /**
     * This deletes the short url data from backend datasource
     *
     * @param shortUrl
     */
    @DeleteMapping("/api/deleteUrl/{shortUrl}")
    void getDeleteUrl(@PathVariable String shortUrl) {
        service.deleteUrl(shortUrl);
    }


    /**
     * This method gets the long url for the given short url and redirect to the same
     *
     * @param shortUrl
     * @return status code 302 if short URL found else 404 code is returned
     */
    @GetMapping("/{shortUrl}")
    ResponseEntity getUrl(@PathVariable String shortUrl) {
        logger.info("Received lookup request for short URL : {}", shortUrl);

        Optional<ShortUrl> url = service.findAndUpdateUrl(shortUrl);

        if (url.isPresent()) {
            logger.info("Found long URL : {} for short URL : {}", url.get().getUrl(), shortUrl);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url.get().getUrl()))
                    .build();
        } else {
            logger.warn("Long URL not found for short URL : {}", shortUrl);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
