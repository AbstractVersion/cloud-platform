/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.templates.response;

/**
 *
 * @author onelove
 */
public class Message {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Message(String status) {
        this.status = status;
    }

    public Message() {
    }

}
