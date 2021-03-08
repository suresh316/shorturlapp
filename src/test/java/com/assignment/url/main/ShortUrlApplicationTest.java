package com.assignment.url.main;

import com.assignment.url.dao.LongUrl;
import com.assignment.url.dao.ShortUrlDataSource;
import com.assignment.url.data.TestData;
import com.assignment.url.service.UrlService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.assignment.url.main.ShortUrlApplication.class)
class ShortUrlApplicationTest extends TestData {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ShortUrlDataSource dataSource;

    @MockBean
    private UrlService service;

    @Test
    public void helloMessageTest() {
        String result = restTemplate.getForObject("http://localhost:" + port + "/",
                String.class);
        Assertions.assertEquals("<h1>Hello !! this is short url App <h1>", result);
        System.out.println(result);
    }

    @Test
    public void findUrlTest() {
        String result = restTemplate.getForObject("http://localhost:" + port + "/api/getUrl/abcd",
                String.class);
        Assertions.assertEquals(null, result);
    }

    @Test
    public void deleteUrlTest() {
        doNothing().when(dataSource).deleteById(isA(String.class));
        restTemplate.delete("http://localhost:" + port + "/api/deleteUrl/abcd");
    }

    @Test
    public void createUrlTest() {
        when(service.createShortUrl(any())).thenReturn(stub);
        ResponseEntity result = restTemplate.postForEntity("http://localhost:" + port + "/api/createShortUrl",
                new LongUrl("https://google.com/test", "test"), String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }


    @Test
    public void getUrlTest() {
        ResponseEntity result = restTemplate.getForEntity("http://localhost:" + port + "/abc",
                String.class);
        Assertions.assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void getUrl2Test() {
        when(service.findAndUpdateUrl(any())).thenReturn(Optional.of(stub));
        ResponseEntity result = restTemplate.getForEntity("http://localhost:" + port + "/zX2ZK",
                String.class);
        Assertions.assertEquals(302, result.getStatusCodeValue());
    }

    @Test
    public void getMostUsedUrlTest() {
        when(dataSource.findAll()).thenReturn(urlList);
        ResponseEntity result = restTemplate.getForEntity("http://localhost:" + port + "/api/getMostUsedUrl",
                String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void getMostRecentCreatedUrlTest() {
        when(dataSource.findAll()).thenReturn(urlList);
        ResponseEntity result = restTemplate.getForEntity("http://localhost:" + port + "/api/getRecentCreatedUrl",
                String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    public void getUrlByUserTest() {
        when(dataSource.findAll()).thenReturn(urlList);
        ResponseEntity result = restTemplate.getForEntity("http://localhost:" + port + "/api/getUrlByUser/suresh",
                String.class);
        Assertions.assertEquals(200, result.getStatusCodeValue());
    }
}