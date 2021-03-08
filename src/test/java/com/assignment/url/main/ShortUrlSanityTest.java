package com.assignment.url.main;

import com.assignment.url.controller.ShortUrlController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = com.assignment.url.main.ShortUrlApplication.class)
public class ShortUrlSanityTest {

    @Autowired
    private ShortUrlController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}
