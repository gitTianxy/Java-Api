package com.java.api.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnirestTest {

    @Test
    public void testGet() throws UnirestException {
        String url;
        Map<String, String> headers;
        Map<String, String> rtParams;
        HttpResponse<JsonNode> response;
        url = "http://localhost:8088/rest/jpa_entity/{id}";
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        rtParams = new HashMap<>();
        rtParams.put("id", "1");
        response = UnirestDemo.doGet(url, headers, rtParams, null);
        System.out.println(String.format("GET. status:%s, body:%s", response.getStatus(), response.getBody()));
    }

    @Test
    public void testPost() throws IOException, UnirestException {
        String url;
        Map<String, String> headers;
        Map<String, String> rtParams;
        HttpResponse<JsonNode> response;
        url = "http://localhost:8088/rest/jpa_entity";
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Map<String, Object> bdParams = new HashMap<>();
        bdParams.put("fieldA", "urirest-test fA");
        bdParams.put("fieldB", "urirest-test fB");
        response = UnirestDemo.doPost(url, headers, null, bdParams);
        System.out.println(String.format("POST. status:%s, body:%s", response.getStatus(), response.getBody()));
    }

    @Test
    public void testPut() throws IOException, UnirestException {
        String url;
        Map<String, String> headers;
        Map<String, String> rtParams;
        HttpResponse<JsonNode> response;
        url = "http://localhost:8088/rest/jpa_entity/{id}";
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        rtParams = new HashMap<>();
        rtParams.put("id", "2");
        Map<String, Object> bdParams = new HashMap<>();
        bdParams.put("fieldA", "urirest-test fA NEW");
        bdParams.put("fieldB", "urirest-test fB NEW");
        response = UnirestDemo.doPut(url, headers, rtParams, bdParams);
        System.out.println(String.format("PUT. status:%s, body:%s", response.getStatus(), response.getBody()));
    }

    @Test
    public void testDelete() throws UnirestException {
        String url;
        Map<String, String> headers;
        Map<String, String> rtParams;
        HttpResponse<JsonNode> response;
        url = "http://localhost:8088/rest/jpa_entity/{id}";
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        rtParams = new HashMap<>();
        rtParams.put("id", "2");
        response = UnirestDemo.doDelete(url, headers, rtParams);
        System.out.println(String.format("DELETE. status:%s, body:%s", response.getStatus(), response.getBody()));
    }
}
