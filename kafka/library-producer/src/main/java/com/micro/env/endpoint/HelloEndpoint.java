/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.endpoint;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author onelove
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RefreshScope
public class HelloEndpoint {

    private final String message = "Hello from HelloService";
    @Autowired
    private Environment environment;
    @Autowired
    private DiscoveryClient client;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> hello() throws UnknownHostException {
        logger.info("Ta kataferame dikie mou {}", "skatoules");
        return ResponseEntity.ok(
                "ok"
        );
    }

}
