package com.gateway.api;

import com.gateway.api.conf.filters.PostConstractRequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.gateway.api.conf.filters.PreRequestFilter;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class GatewayApiZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApiZuulApplication.class, args);
    }
    
//  @Bean
//  public PreRequestFilter preRequestFilter() {
//    return new PreRequestFilter();
//  }
  @Bean
  public PostConstractRequestFilter postRequestFilter() {
    return new PostConstractRequestFilter();
  }

}
