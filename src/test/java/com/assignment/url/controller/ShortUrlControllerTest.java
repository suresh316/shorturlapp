package com.assignment.url.controller;

import com.assignment.url.dao.ShortUrlDataSource;
import com.assignment.url.data.TestData;
import com.assignment.url.main.ShortUrlApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortUrlController.class)
@ContextConfiguration(classes = {ShortUrlApplication.class})
public class ShortUrlControllerTest extends TestData {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShortUrlController service;


    @MockBean
    private ShortUrlDataSource dataSource;

    @Test
    public void getFullUrlTest() throws Exception {
        mockMvc.perform(get("/api/getUrl/abcd")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void getUrlTest() throws Exception {
        mockMvc.perform(get("/abcd")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void deleteUrlTest() throws Exception {
        doNothing().when(dataSource).deleteById(isA(String.class));
        mockMvc.perform(delete("/api/deleteUrl/abcd")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void defaultUrlTest() throws Exception {
        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void createUrlTest() throws Exception {
        when(dataSource.save(any())).thenReturn(stub);
        mockMvc.perform(post("/api/createShortUrl", "https://google.com")).andDo(print());
    }

}
