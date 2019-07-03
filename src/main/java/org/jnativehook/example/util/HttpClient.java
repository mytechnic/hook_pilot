package org.jnativehook.example.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class HttpClient {
    private static String userAgent;
    private static String contentType;

    public HttpClient() {
        userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36";
        contentType = "application/json";
    }

    public String post(String url, String body) throws IOException {

        log.debug("url : {}", url);
        log.debug("body : {}", body);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", userAgent);
        httpPost.setHeader("Content-Type", contentType);

        StringEntity params = new StringEntity(body, "UTF-8");
        httpPost.setEntity(params);

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        httpPost.releaseConnection();

        return responseString;
    }

    public String get(String url, String referer) throws IOException {

        log.debug("url : {}", url);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", userAgent);

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        httpGet.releaseConnection();

        log.debug("responseString = {}", responseString);

        return responseString;
    }
}
