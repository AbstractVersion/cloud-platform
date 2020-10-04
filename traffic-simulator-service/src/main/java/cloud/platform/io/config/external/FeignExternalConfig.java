package cloud.platform.io.config.external;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author George Fiotakis
 */
@Component
@ConfigurationProperties(prefix = "feign-ext")
public class FeignExternalConfig {

    private long retryPeriod;
    private long retryMaxPeriod;
    private int retryMaxAttempts;
    
    private int requestConnectTimeoutMill;
    private int readTimeoutMill;
    

    public FeignExternalConfig() {
    }
    

    public long getRetryPeriod() {
        return retryPeriod;
    }

    public long getRetryMaxPeriod() {
        return retryMaxPeriod;
    }

    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }

    public int getRequestConnectTimeoutMill() {
        return requestConnectTimeoutMill;
    }

    public int getReadTimeoutMill() {
        return readTimeoutMill;
    }

    public void setRetryPeriod(long retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    public void setRetryMaxPeriod(long retryMaxPeriod) {
        this.retryMaxPeriod = retryMaxPeriod;
    }

    public void setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
    }

    public void setRequestConnectTimeoutMill(int requestConnectTimeoutMill) {
        this.requestConnectTimeoutMill = requestConnectTimeoutMill;
    }

    public void setReadTimeoutMill(int readTimeoutMill) {
        this.readTimeoutMill = readTimeoutMill;
    }
    
    
    
    
    
    
}
