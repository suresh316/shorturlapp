package com.assignment.url.service;

import com.assignment.url.dao.LongUrl;
import com.assignment.url.dao.ShortUrl;
import com.assignment.url.dao.ShortUrlDataSource;
import com.assignment.url.data.TestData;
import com.assignment.url.util.UrlNotPresent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;


@SpringBootTest(classes = com.assignment.url.service.UrlService.class)
public class UrlServiceTest extends TestData {

    @Autowired
    private UrlService service;

    @MockBean
    private ShortUrlDataSource dataSource;

    public void generateShortUrlTest() {
        String s1 = service.generateShortUrl(url, 1);
        Assertions.assertEquals("zX2ZK", s1);
    }

    @Test
    public void trim1Test() {
        String s1 = service.trim("https://facebook.com/");
        Assertions.assertEquals("https://facebook.com", s1);
    }

    @Test
    public void trim2Test() {
        String s1 = service.trim("https://facebook.com/////");
        Assertions.assertEquals("https://facebook.com", s1);
    }

    @Test
    public void getShortUrlTest() {
        when(dataSource.findById(any())).thenReturn(Optional.of(stub));
        ShortUrl shortUrl = service.createShortUrl(url);
        Assertions.assertEquals("zX2ZK", shortUrl.getShorUrl());
    }

    @Test
    public void getShortUrl2Test() {
        when(dataSource.findById(any()))
                .thenReturn(Optional.ofNullable(null))
                .thenReturn(Optional.of(stub))
                .thenReturn(Optional.of(stub3))
                .thenReturn(Optional.of(stub2));
        ShortUrl shortUrl = service.createShortUrl(new LongUrl(longUrl + "new", "test"));
        Assertions.assertEquals("zX2ZK", shortUrl.getShorUrl());
    }

    @Test
    public void getFindUrlTest() {
        Optional<ShortUrl> opStub = Optional.of(stub);
        when(dataSource.findById("zX2ZK")).thenReturn(opStub);
        Optional<ShortUrl> shortUrl = service.findUrl("zX2ZK");
        Assertions.assertEquals(stub.getUrl(), shortUrl.get().getUrl());
        Assertions.assertEquals(stub.getCreationTime(), shortUrl.get().getCreationTime());
        Assertions.assertEquals(stub.getUser(), shortUrl.get().getUser());
        Assertions.assertEquals(stub.getNumOfHits(), shortUrl.get().getNumOfHits());
        Assertions.assertEquals("zX2ZK", shortUrl.get().getShorUrl());
    }

    @Test
    public void getFindAndUpdateUrlTest() {
        Optional<ShortUrl> opStub = Optional.of(stub);
        Optional<ShortUrl> opStub1 = Optional.of(stub3);
        when(dataSource.findById("zX2ZK")).thenReturn(opStub).thenReturn(opStub1);
        when(dataSource.save(stub)).thenReturn(stub);
        Optional<ShortUrl> shortUrl = service.findAndUpdateUrl("zX2ZK");
        Assertions.assertEquals("zX2ZK", shortUrl.get().getShorUrl());
    }

    @Test
    public void getFindAndUpdateUrl2Test() {
        Optional<ShortUrl> opStub = Optional.ofNullable(null);
        when(dataSource.findById("zX2ZK")).thenReturn(opStub);
        when(dataSource.save(stub)).thenReturn(stub);
        Optional<ShortUrl> shortUrl = service.findAndUpdateUrl("zX2ZK");
        Assertions.assertFalse(shortUrl.isPresent());
    }

    @Test
    public void getFindAndUpdateUrl3Test() {
        Optional<ShortUrl> opStub = Optional.of(stub);
        String uuId1 = UUID.randomUUID().toString();
        String uuId2 = UUID.randomUUID().toString();
        long updateTime1 = Instant.now().toEpochMilli();
        long updateTime2 = (long) (Instant.now().toEpochMilli() * Math.random());
        stub.setUpdateId(uuId1);
        stub.setLastAccessedTime(updateTime1);
        stub3.setUpdateId(uuId2);
        stub3.setLastAccessedTime(updateTime2);
        stub4.setUpdateId(uuId2);
        stub4.setLastAccessedTime(updateTime2);
        Optional<ShortUrl> opStub2 = Optional.of(stub3);
        Optional<ShortUrl> opStub3 = Optional.of(stub4);

        when(dataSource.findById("zX2ZK")).thenReturn(opStub)
                .thenReturn(opStub2).thenReturn(opStub3);
        when(dataSource.save(stub)).thenReturn(stub);
        Optional<ShortUrl> shortUrl = service.findAndUpdateUrl("zX2ZK");
        Assertions.assertEquals("zX2ZK", shortUrl.get().getShorUrl());
    }

    @Test
    public void deleteTest() {
        when(dataSource.existsById(any())).thenReturn(false);
        Assertions.assertThrows(UrlNotPresent.class, () -> service.deleteUrl("XFOX8"));
    }

    @Test
    public void delete2Test() {
        when(dataSource.existsById(any())).thenReturn(true);
        service.deleteUrl("XFOX8");
        verify(dataSource, times(1)).deleteById("XFOX8");
    }

    @Test
    public void getMostRecentCreatedUsedUrlTest() {
        when(dataSource.findAll()).thenReturn(urlList);
        ShortUrl url = service.getMostRecentCreatedUrl();
        Assertions.assertEquals(stub3, url);
    }

    @Test
    public void getMostUsedUrlTest() {
        when(dataSource.findAll()).thenReturn(urlList);
        ShortUrl url = service.getMostUsedUrl();
        Assertions.assertEquals(stub4, url);
    }

    @Test
    public void getUrlByUser() {
        when(dataSource.findAll()).thenReturn(urlList);
        List<ShortUrl> urlList = service.getUrlByUser("suresh");
        Assertions.assertEquals(2, urlList.size());
    }

}
