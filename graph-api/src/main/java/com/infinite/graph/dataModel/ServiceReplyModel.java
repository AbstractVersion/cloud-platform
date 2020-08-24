/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.dataModel;

/**
 *
 * @author onelove
 */
public class ServiceReplyModel {

    private String serviceUUID;
    private String serviceHost;

    public ServiceReplyModel(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public ServiceReplyModel(String serviceUUID, String host) {
        this.serviceUUID = serviceUUID;
        this.serviceHost = host;
    }

    public String getServiceUUID() {
        return serviceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.serviceUUID = serviceUUID;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public void setServiceHost(String serviceHost) {
        this.serviceHost = serviceHost;
    }

}
