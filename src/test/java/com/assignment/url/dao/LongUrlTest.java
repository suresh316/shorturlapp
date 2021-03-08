package com.assignment.url.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LongUrlTest {


    private static LongUrl url1;
    private static LongUrl url2;
    private static LongUrl url3;

    @BeforeAll
    public static void setup() {
        url1 = new LongUrl("https://google.com/news", "tester1");
        url2 = new LongUrl("https://google.com/news", "tester1");
        url3 = new LongUrl("https://google.com/news", "tester2");
    }


    @Test
    public void equalsTest() {
        Assertions.assertTrue(url1.equals(url2));
    }

    @Test
    public void equals2Test() {
        Assertions.assertNotEquals(url1, url3);
    }

    @Test
    public void equals3Test() {
        Assertions.assertNotEquals(url1, null);
    }

    @Test
    public void equals4Test() {
        Assertions.assertNotEquals(url1, new Object());
    }

    @Test
    public void equals5Test() {
        Assertions.assertEquals(url1, url1);
    }


    @Test
    public void hashCodeTest() {
        Assertions.assertEquals(url1.hashCode(), url2.hashCode());
    }

    @Test
    public void hashCode2Test() {
        Assertions.assertNotEquals(url1.hashCode(), url3.hashCode());
    }

}
