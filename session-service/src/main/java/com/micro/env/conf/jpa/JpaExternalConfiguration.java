/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.conf.jpa;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author onelove
 */
@Component
@ConfigurationProperties(prefix = "db-connection")
public class JpaExternalConfiguration {

    private String url;
    private String driverClass;
    private String basePackage;

    public String getUrl() {
        return url;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

}
