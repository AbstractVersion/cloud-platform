/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.template;

/**
 *
 * @author onelove
 */
public class HelloMessage {

    private String message;
    private int port;
    private String serviceID;
    private String serviceHost;
    private ConfTemplate config;

    public HelloMessage() {
    }

    public HelloMessage(String message, int port, String serviceID) {
        this.message = message;
        this.port = port;
        this.serviceID = serviceID;
    }

    public HelloMessage(String message, int port, String serviceID, String serviceHost) {
        this.message = message;
        this.port = port;
        this.serviceID = serviceID;
        this.serviceHost = serviceHost;
    }

    public HelloMessage(String message, int port, String serviceID, String serviceHost, ConfTemplate config) {
        this.message = message;
        this.port = port;
        this.serviceID = serviceID;
        this.serviceHost = serviceHost;
        this.config = config;
    }

    public ConfTemplate getConfig() {
        return config;
    }

    public void setConfig(ConfTemplate config) {
        this.config = config;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

    public String getMessage() {
        return message;
    }

    public int getPort() {
        return port;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

}
