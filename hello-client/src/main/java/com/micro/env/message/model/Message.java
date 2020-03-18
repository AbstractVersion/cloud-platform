/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.message.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author onelove
 */
public class Message implements Serializable {

    private static final long serialVersionUID = -214234234342324L;

    private Date timestamp;
    private String message;

    public Message() {
        this.timestamp = new Date();
    }

    public Message(String message) {
        this.timestamp = new Date();
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
