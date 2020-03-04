package com.infinite.graph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GraphApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphApiApplication.class, args);
	}

}
