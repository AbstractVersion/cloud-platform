/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hallo.ivocation;

import com.hallo.ivocation.template.PythonAPITemplate;

/**
 *
 * @author onelove
 */
public class MessageInvocation {

    private HelloMessage helloMssage;
    private PythonAPITemplate pythonMessage;
    private String invocationServiceID;
    private String invocationServiceHost;
    private int invocationServicePort;

    public MessageInvocation(HelloMessage helloMssage, String invocationServiceID, String invocationServiceHost, int invocationServicePort) {
        this.helloMssage = helloMssage;
        this.invocationServiceID = invocationServiceID;
        this.invocationServiceHost = invocationServiceHost;
        this.invocationServicePort = invocationServicePort;
    }
    public MessageInvocation(PythonAPITemplate helloMssage, String invocationServiceID, String invocationServiceHost, int invocationServicePort) {
        this.pythonMessage = helloMssage;
        this.invocationServiceID = invocationServiceID;
        this.invocationServiceHost = invocationServiceHost;
        this.invocationServicePort = invocationServicePort;
    }

    public PythonAPITemplate getPythonMessage() {
        return pythonMessage;
    }

    public void setPythonMessage(PythonAPITemplate pythonMessage) {
        this.pythonMessage = pythonMessage;
    }
    
    

    public MessageInvocation() {
    }

    public void setHelloMssage(HelloMessage helloMssage) {
        this.helloMssage = helloMssage;
    }

    public void setInvocationServiceID(String invocationServiceID) {
        this.invocationServiceID = invocationServiceID;
    }

    public void setInvocationServiceHost(String invocationServiceHost) {
        this.invocationServiceHost = invocationServiceHost;
    }

    public void setInvocationServicePort(int invocationServicePort) {
        this.invocationServicePort = invocationServicePort;
    }

    public HelloMessage getHelloMssage() {
        return helloMssage;
    }

    public String getInvocationServiceID() {
        return invocationServiceID;
    }

    public String getInvocationServiceHost() {
        return invocationServiceHost;
    }

    public int getInvocationServicePort() {
        return invocationServicePort;
    }

}
