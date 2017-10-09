package com.java.api.apache.http;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevintian on 2017/10/9.
 */
public class HttpClientTest {
    HttpClientDemo demo;

    @Before
    public void before() {
        demo = new HttpClientDemo();
    }

    @Test
    public void testGet() {
        String url = "https://www.baidu.com/";
        demo.get(url);
    }

    @Test
    public void testPost() {
        String postUrl = "http://localhost:8088/rest/jpa_entity";
        Map<String, String> postHeaders = new HashMap<>();
        postHeaders.put("Content-Type", "application/json");
        Map<String, Object> postBody = new HashMap<>();
        long ts = System.currentTimeMillis();
        postBody.put("fieldA", "fA" + ts);
        postBody.put("fieldB", "fB" + ts);
        demo.post(postUrl, postHeaders, postBody);
    }

    @Test
    public void testGetWithProxy() {
        String uri = "https://www.google.com";
        HttpClientDemo.Proxy proxyParams = demo.new Proxy("127.0.0.1", 1080);
        demo.getWithProxy(uri, proxyParams);
    }
}
