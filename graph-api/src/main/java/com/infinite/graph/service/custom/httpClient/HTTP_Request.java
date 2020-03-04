/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.service.custom.httpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 *
 * @author onelove
 */
public class HTTP_Request {

    protected HttpURLConnection con;
    protected BufferedReader in;
    protected OutputStreamWriter out;
    protected Map<String, String> headers;

    public HTTP_Request() {
        headers = new HashMap<>();
    }

    protected void create_get_request(URL url) throws MalformedURLException, ProtocolException, IOException {
        this.con = (HttpURLConnection) url.openConnection();
        this.con.setRequestMethod("GET");
    }

    protected void insertBasicAuthToURL(String uname, String pwd) {
        this.con.setRequestProperty("Authorization", "Basic " + encode_credentials(uname, pwd));
    }

    protected void insertBearerAuthToURL(String token) {
        this.con.setRequestProperty("Authorization", "Bearer " + token);
    }

    protected String encode_credentials(String username, String password) {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));  //Java 8

    }

    protected void addRequestHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.con.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    protected HttpHeaders createBasiceHttpHeaders(String user, String password) {
        String notEncoded = user + ":" + password;
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(notEncoded.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", encodedAuth);
        return headers;
    }

    protected HttpHeaders createBearereHttpHeaders(String token) {
        String bearer_header = "Bearer " + token;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", bearer_header);
        return headers;
    }
    protected HttpHeaders basicJsonContentTypeHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
