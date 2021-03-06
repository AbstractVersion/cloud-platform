/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hallo.ivocation;

import brave.Tracer;
import org.springframework.context.annotation.Bean;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @authorpre onelove
 */
public class FeignConfiguration {
   @Autowired
    private Tracer tracer;
   
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(FeignConfiguration.class.getName());

    @Bean
    public Logger.Level configureLogLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options timeoutConfiguration() {
        return new Request.Options(5000, 30000);
    }

    @Bean
    public RequestInterceptor requestLoggingInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                LOG.info("Adding header [testHeader / testHeaderValue] to request");
                template.header("request-trace-id", tracer.currentSpan().context().traceIdString());
            }
        };
    }

//    BASIC AUTH
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("user", "password");
//    }
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 8000, 3);
    }
}
