package com.assignment.url.util;

/**
 * This is custom run time exception and will thrown if the
 * given short URL not found in data source
 */
public class UrlNotPresent extends RuntimeException {

    public UrlNotPresent(String url) {
        super(url + " Url not present in datasource");
    }

}
