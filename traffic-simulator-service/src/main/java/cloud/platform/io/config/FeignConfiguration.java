package cloud.platform.io.config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.context.annotation.Bean;
import cloud.platform.io.config.external.FeignExternalConfig;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author onelove
 */
@Slf4j
public class FeignConfiguration {

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
