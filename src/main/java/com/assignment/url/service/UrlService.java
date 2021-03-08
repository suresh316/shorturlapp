package com.assignment.url.service;

import com.assignment.url.controller.ShortUrlController;
import com.assignment.url.dao.LongUrl;
import com.assignment.url.dao.ShortUrl;
import com.assignment.url.dao.ShortUrlDataSource;
import com.assignment.url.util.UrlNotPresent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UrlService {

    Logger logger = LogManager.getLogger(UrlService.class);

    @Value("${short.url.length}")
    private int shortUrlLength;

    /**
     * Represent 64 chars which will used to encode/generate
     * short URL
     */
    private static final char[] base64URL = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };


    @Autowired
    ShortUrlDataSource dataSource;

    /**
     * THis method removes trailing '/'
     *
     * @param url
     * @return trimmed url without trailing /
     */
    public String trim(String url) {
        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * This method gets the short URL and save to backed data source/database
     *
     * @param url
     * @return
     */
    public ShortUrl createShortUrl(LongUrl url) {
        String shortUrl = generateShortUrl(url, 1);
        Optional<ShortUrl> persistedUrl = dataSource.findById(shortUrl);
        if (!persistedUrl.isPresent()) {
            ShortUrl s = new ShortUrl(shortUrl, url.getUrl(), Instant.now().toEpochMilli(), url.getUser(), 0);
            dataSource.save(s);
            logger.info("ShortUrl : {} saved to DB successfully for URL : {} ", s, url);
        }
        persistedUrl = dataSource.findById(shortUrl);
        // Compare , generate and save till we get the
        // persistedUrl with same long url and user
        while (persistedUrl.isPresent() && (!persistedUrl.get().getUrl().equals(url.getUrl())
                || !persistedUrl.get().getUser().equals(url.getUser()))) {
            shortUrl = generateShortUrl(url, Instant.now().toEpochMilli());
            ShortUrl s = new ShortUrl(shortUrl, url.getUrl(), Instant.now().toEpochMilli(), url.getUser(), 0);
            dataSource.save(s);
            logger.info("Collision occurred.  Short URL regenerated {} and saved to DB successfully for URL : {}", persistedUrl.isPresent(), url);
            persistedUrl = dataSource.findById(shortUrl);
        }

        return persistedUrl.get();
    }

    /**
     * This method generates short url for the given long url and user
     *
     * @param url
     * @param multiplier
     * @return short url
     */
    public String generateShortUrl(LongUrl url, long multiplier) {
        // use hashcode and divide it by 64 and use the
        // remainder to get character from base64 char array
        long hash = url.hashCode() * multiplier;
        StringBuilder builder = new StringBuilder();
        int count = 0;
        while (count++ < shortUrlLength) {
            int rem = (int) Math.abs(hash % base64URL.length);
            hash = hash / base64URL.length;
            builder.append(base64URL[rem]);
        }
        return builder.toString();
    }


    /**
     * This method runs List of ShortUrl object for the given user
     *
     * @param user
     * @return List of ShortUrl
     */
    public List<ShortUrl> getUrlByUser(String user) {
        return dataSource.findAll()
                .stream().filter(s -> s.getUser().equals(user)).
                        collect(Collectors.toList());

    }

    /**
     * This method runs returns the most used ShortUrl
     *
     * @return List of ShortUrl
     */
    public ShortUrl getMostUsedUrl() {
        return dataSource.findAll()
                .stream().sorted(Comparator.comparingLong(ShortUrl::getNumOfHits).reversed())
                .findFirst().orElseGet(null);
    }

    /**
     * This method  returns the most recently created short URL
     *
     * @return List of ShortUrl
     */
    public ShortUrl getMostRecentCreatedUrl() {
        return dataSource.findAll()
                .stream().sorted(Comparator.comparingLong(ShortUrl::getCreationTime).reversed())
                .findFirst().orElseGet(null);
    }


    /**
     * This method runs ShortUrl object for the given shortUrl is present in datasource else
     * null is return
     *
     * @param shortUrl
     * @return ShortUrl is present else  null
     */
    public Optional<ShortUrl> findUrl(String shortUrl) {
        return dataSource.findById(shortUrl);
    }

    /**
     * This method return ShortUrl object for the given shortUrl if present in datasource
     * and increments the hits and save again to datasource else return null
     * else  is return and
     *
     * @return ShortUrl is present else  null
     */
    public Optional<ShortUrl> findAndUpdateUrl(String shortUrl) {
        Optional<ShortUrl> url = dataSource.findById(shortUrl);
        ShortUrl sUrl = null;
        if (url.isPresent()) {
            sUrl = url.get();
            long updateTime = Instant.now().toEpochMilli();
            String updateId = UUID.randomUUID().toString();
            sUrl.incrementNumOfHits();
            long incrementedHits = sUrl.getNumOfHits();
            sUrl.setUpdateId(updateId);
            sUrl.setLastAccessedTime(updateTime);
            dataSource.save(sUrl);
            sUrl = dataSource.findById(shortUrl).get();
            while (sUrl.getNumOfHits() != incrementedHits ||
                    !updateId.equals(sUrl.getUpdateId()) ||
                    updateTime != sUrl.getLastAccessedTime()) {
                logger.info("Collision occurred while updating Short URL {}. Updating it again.", sUrl);
                updateTime = Instant.now().toEpochMilli();
                updateId = UUID.randomUUID().toString();
                sUrl.incrementNumOfHits();
                incrementedHits = sUrl.getNumOfHits();
                sUrl.setUpdateId(updateId);
                sUrl.setLastAccessedTime(updateTime);
                dataSource.save(sUrl);
                sUrl = dataSource.findById(shortUrl).get();
            }
            logger.info("ShortUrl : {} updated to DB successfully", sUrl);
        }
        return Optional.ofNullable(sUrl);
    }


    /**
     * This method delete the short url data from backend data source
     *
     * @param shortUrl
     */
    public void deleteUrl(String shortUrl) {
        if (!dataSource.existsById(shortUrl)) {
            throw new UrlNotPresent(shortUrl);
        }
        dataSource.deleteById(shortUrl);
    }
}
