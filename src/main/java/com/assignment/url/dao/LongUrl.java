package com.assignment.url.dao;

import org.springframework.lang.NonNull;

import java.util.Objects;

public class LongUrl {

    @NonNull
    String url;

    @NonNull
    String user;

    public LongUrl(String url, String user) {
        this.url = url;
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongUrl longUrl = (LongUrl) o;
        return url.equals(longUrl.url) && user.equals(longUrl.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, user);
    }

    @Override
    public String toString() {
        return "LongUrl{" +
                "url='" + url + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
