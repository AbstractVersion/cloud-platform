/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.conf;

import com.micro.env.conf.externalConfig.FeignExternalConfig;
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
 * @author onelove
 */
public class FeignConfiguration {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(FeignConfiguration.class.getName());

    @Autowired
    private FeignExternalConfig config;

    @Bean
    public Logger.Level configureLogLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public Request.Options timeoutConfiguration() {
        return new Request.Options(
                config.getRequestConnectTimeoutMill(),
                config.getReadTimeoutMill()
        );
    }

//    @Bean
//    public RequestInterceptor requestLoggingInterceptor() {
//        return new RequestInterceptor() {
//            @Override
//            public void apply(RequestTemplate template) {
//                LOG.info("Adding header [testHeader / testHeaderValue] to request");
//                template.header("testHeader", "testHeaderValue");
//            }
//        };
//    }
//    BASIC AUTH
//    @Bean
//    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
//        return new BasicAuthRequestInterceptor("user", "password");
//    }
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                config.getRetryPeriod(),
                config.getRetryMaxPeriod(),
                config.getRetryMaxAttempts()
        );
    }
}
