/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.service.custom.httpClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinite.graph.exception.NotEnoughPrevilagesException;
import com.infinite.graph.exception.UserTokenExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author onelove
 */
@Controller
public class Graph_Handler extends HTTP_Request {

    private static Logger logger = LoggerFactory.getLogger(Graph_Handler.class);
    private String inputLine;
    private RestTemplate restTemplate;

    private ObjectMapper mapper;

    public Graph_Handler() {
        super();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Object requestRESTInformationGET(String url, String token, Class<?> class_type) {
        restTemplate = new RestTemplate();
        HttpStatus response_code = null;
        logger.info("Performing GET request at : " + url.toString());
        try {
            ResponseEntity<?> response = restTemplate.
                    exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<String>("parameters", this.createBearereHttpHeaders(token)),
                            class_type
                    );
            response_code = response.getStatusCode();
            return response.getBody();
        } catch (Exception eek) {
            if (response_code == HttpStatus.UNAUTHORIZED) {
                logger.info(
                        "Microsoft Graph has returned 401, probably token is expired or invalid : ",
                        new UserTokenExpiredException("The user token has been expired."));
            } else if (response_code == HttpStatus.FORBIDDEN) {
                logger.info(
                        "The user does not have enough previlages to perform the requested action",
                        new NotEnoughPrevilagesException("Not enough user privilages to perform the requested action."));
            }
        }
        return null;
    }

}
