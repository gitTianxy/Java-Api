package com.java.api.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesTest {

    @Test
    public void testFiles() throws IOException {
        final String ROOT = "/Users/kevin/Desktop/io_test";
        Path root = Paths.get(ROOT);
        Files.list(root).forEach(path -> {
            if (path.getFileName().toString().contains("tmp")) {
                try {
                    System.out.println("delete old tmp:" + path.getFileName());
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testURLInput() throws IOException, URISyntaxException {
        URL url = new URL("https://www.baidu.com");
        URLConnection connection = url.openConnection();

        InputStream in = connection.getInputStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String content = reader.readLine();
            while (content != null) {
                System.out.println(content);
                content = reader.readLine();
            }
        }
        Map<String, List<String>> headers = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> hd : headers.entrySet()) {
            System.out.println(String.format("head: key=%s, value=%s", hd.getKey(), hd.getValue()));
        }
    }

    @Test
    public void testURLOutput() throws IOException {
        URL target = new URL("http://localhost:8088/rest/jpa_entity");
        HttpURLConnection httpConn = (HttpURLConnection) target.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("Content-Type", "application/json");
        httpConn.setRequestProperty("Accept-Charset", "utf-8");
        Map<String, Object> bdParams = new HashMap<>();
        bdParams.put("fieldA", "urirest-test fA");
        bdParams.put("fieldB", "urirest-test fB");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpConn.getOutputStream()))) {
            writer.write(new ObjectMapper().writeValueAsString(bdParams));
            writer.flush();
        }
        System.out.println("status: "+ httpConn.getResponseCode());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
            String response = reader.readLine();
            while (response != null) {
                System.out.println(String.format(response));
                response = reader.readLine();
            }
        }
    }
}
