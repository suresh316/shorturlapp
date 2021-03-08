package com.assignment.url.dao;

import javax.persistence.*;
import java.time.Instant;

/**
 * This entity represent table in the backend datasource
 * and stores short rule details
 */
@Entity
public class ShortUrl {

    @Id
    String shorUrl;

    @Column (length = 2048)
    String url;

    long creationTime;

    String user;

    @GeneratedValue(strategy= GenerationType.AUTO)
    long numOfHits;

    long lastAccessedTime;

    String updateId;

    public ShortUrl() {

    }

    public ShortUrl(String shorUrl, String url, long creationTime, String user, long numOfHits) {
        this.shorUrl = shorUrl;
        this.url = url;
        this.creationTime = creationTime;
        this.user = user;
        this.numOfHits = numOfHits;
    }

    public String getShorUrl() {
        return shorUrl;
    }

    public String getUrl() {
        return url;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getUser() {
        return user;
    }

    public long getNumOfHits() {
        return numOfHits;
    }

    public void incrementNumOfHits() {
        numOfHits++;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    @Override
    public String toString() {
        return "ShortUrl{" +
                "shorUrl='" + shorUrl + '\'' +
                ", url='" + url + '\'' +
                ", creationTime=" + Instant.ofEpochMilli(creationTime) +
                ", user='" + user + '\'' +
                ", numOfHits=" + numOfHits +
                ", lastAccessedTime=" + Instant.ofEpochMilli(lastAccessedTime) +
                ", updateId='" + updateId + '\'' +
                '}';
    }
}
