package com.micro.env;

import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HelloServiceApplication {

    public static String runtimeServiceId;

    public static void main(String[] args) {
        runtimeServiceId = UUID.randomUUID().toString();

        SpringApplication.run(HelloServiceApplication.class, args);
    }

}
