package com.java.api.unirest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.io.IOException;
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
    static HttpResponse doDelete(String url, Map<String, String> headers, Map<String, String> rtParams) throws UnirestException {
        HttpRequestWithBody request = Unirest.delete(url);
        if (headers != null) {
            request.headers(headers);
        }
        if (rtParams != null) {
            for (Map.Entry<String, String> entry : rtParams.entrySet()) {
                request.routeParam(entry.getKey(), entry.getValue());
            }
        }
        return request.asString();
    }

}
