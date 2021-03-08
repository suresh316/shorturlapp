package com.assignment.url.data;

import com.assignment.url.dao.LongUrl;
import com.assignment.url.dao.ShortUrl;
import org.junit.jupiter.api.BeforeAll;

import java.util.LinkedList;
import java.util.List;

public class TestData {

    protected static ShortUrl stub;

    protected static ShortUrl stub2;

    protected static ShortUrl stub3;

    protected static ShortUrl stub4;

    protected static LongUrl url;

    protected static String longUrl;

    protected static List<ShortUrl> urlList;

    @BeforeAll
    public static void setup() {
        longUrl = "https://www.google.com/search?q=champions+league&rlz=1C1CHBF_enIN943IN943&oq=champi&aqs=chrome.1.69i57j35i39i355j0i67i433j0i67j46i175i199j46i131i433j0i433j0i131i433j0i433j46i131i433.4456j1j9&sourceid=chrome&ie=UTF-8#sie=lg;/g/11fks91v3s;2;/m/0c1q0;dt;fp;1;;";

        stub = new ShortUrl("zX2ZK", longUrl, 0, "test", 0);
        stub2 = new ShortUrl("zX2ZK", longUrl + "new", 0, "test", 0);
        stub3 = new ShortUrl("zX2ZK", longUrl + "new", 1000, "suresh", 1);
        stub4 = new ShortUrl("zX2ZK", longUrl + "stub4", 0, "suresh", 5);
        urlList = new LinkedList<>();
        urlList.add(stub);
        urlList.add(stub2);
        urlList.add(stub3);
        urlList.add(stub4);
        url = new LongUrl(longUrl, "test");

    }

}
