package com.micro.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LibraryProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryProducerApplication.class, args);
	}

}
