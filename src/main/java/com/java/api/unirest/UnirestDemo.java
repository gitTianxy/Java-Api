package com.java.api.unirest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Unirest 是一个轻量级的 HTTP 请求库，涵盖 Node、Ruby、Java、PHP、Python、Objective-C、.NET 等多种语言。可发起 GET, POST, PUT, PATCH, DELETE,
 * HEAD, OPTIONS 请求。
 * <p>
 * Created by kevintian on 2017/9/20.
 */
public class UnirestDemo {
    /**
     * do get request
     *
     * @param url
     * @param headers
     * @param rtParams
     * @param params
     * @return
     * @throws UnirestException
     */
    static HttpResponse<JsonNode> doGet(String url, Map<String, String> headers, Map<String, String> rtParams,
                                        Map<String, Object>
                                                params) throws UnirestException {
        GetRequest request = Unirest.get(url);
        if (headers != null) {
            request.headers(headers);
        }
        if (rtParams != null) {
            for (Map.Entry<String, String> entry : rtParams.entrySet()) {
                request.routeParam(entry.getKey(), entry.getValue());
            }
        }
        if (params != null) {
            request.queryString(params);
        }
        return request.asJson();
    }

    /**
     * do POST request
     *
     * @param url
     * @param headers
     * @param rtParams
     * @param bdParams
     * @return
     * @throws UnirestException
     * @throws IOException
     */
    static HttpResponse<JsonNode> doPost(String url, Map<String, String> headers, Map<String, String> rtParams,
                                         Map<String, Object> bdParams) throws UnirestException, IOException {
        HttpRequestWithBody request = Unirest.post(url);
        if (headers != null) {
            request.headers(headers);
        }
        if (rtParams != null) {
            for (Map.Entry<String, String> entry : rtParams.entrySet()) {
                request.routeParam(entry.getKey(), entry.getValue());
            }
        }
        request.body(new ObjectMapper().writeValueAsString(bdParams));
        return request.asJson();
    }

    /**
     * do PUT request
     *
     * @param url
     * @param headers
     * @param rtParams
     * @param bdParams
     * @return
     * @throws UnirestException
     * @throws IOException
     */
    static HttpResponse<JsonNode> doPut(String url, Map<String, String> headers, Map<String, String> rtParams,
                                        Map<String, Object> bdParams) throws UnirestException, IOException {
        HttpRequestWithBody request = Unirest.put(url);
        if (headers != null) {
            request.headers(headers);
        }
        if (rtParams != null) {
            for (Map.Entry<String, String> entry : rtParams.entrySet()) {
                request.routeParam(entry.getKey(), entry.getValue());
            }
        }
        request.body(new ObjectMapper().writeValueAsString(bdParams));
        return request.asJson();
    }

    /**
     * do DELETE request
     *
     * @param url
     * @param headers
     * @param rtParams
     * @return
     * @throws UnirestException
     */
    static HttpResponse<JsonNode> doDelete(String url, Map<String, String> headers, Map<String, String> rtParams)
            throws UnirestException {
        HttpRequestWithBody request = Unirest.delete(url);
        if (headers != null) {
            request.headers(headers);
        }
        if (rtParams != null) {
            for (Map.Entry<String, String> entry : rtParams.entrySet()) {
                request.routeParam(entry.getKey(), entry.getValue());
            }
        }
        return request.asJson();
    }


    public static void main(String[] args) throws Exception {
        // get
        String url = "http://localhost:8088/rest/jpa_entity/{id}";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Map<String, String> rtParams = new HashMap<>();
        rtParams.put("id", "10");
        HttpResponse<JsonNode> response = doGet(url, headers, rtParams, null);
        int status = response.getStatus();
        JSONObject body = response.getBody().getObject();
        System.out.println(String.format("GET succ. status:%s, body:%s", status, body));
        // post
        url = "http://localhost:8088/rest/jpa_entity";
        Map<String, Object> bdParams = new HashMap<>();
        bdParams.put("fieldA", "urirest-test fA");
        bdParams.put("fieldB", "urirest-test fB");
        response = doPost(url, headers, null, bdParams);
        System.out.println(String.format("POST succ. status:%s, body:%s", response.getStatus(), response.getBody()
                .getObject()));
        // put
        url = "http://localhost:8088/rest/jpa_entity/{id}";
        rtParams = new HashMap<>();
        rtParams.put("id", "12");
        bdParams = new HashMap<>();
        bdParams.put("fieldA", "urirest-test fA NEW");
        bdParams.put("fieldB", "urirest-test fB NEW");
        response = doPut(url, headers, rtParams, bdParams);
        System.out.println(String.format("PUT succ. status:%s, body:%s", response.getStatus(), response.getBody()
                .getObject()));
        // delete
        url = "http://localhost:8088/rest/jpa_entity/{id}";
        rtParams = new HashMap<>();
        rtParams.put("id", "12");
        response = doDelete(url, headers, rtParams);
        System.out.println(String.format("DELETE succ. status:%s, body:%s", response.getStatus(), response.getBody()
                .getObject()));
    }
}
