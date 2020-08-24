/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.repository.remote.temlates.request;

import java.io.Serializable;

/**
 *
 * @author onelove
 */
public class UserToken implements Serializable {

    private String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public UserToken() {
    }

    public UserToken(String userToken) {
        this.userToken = userToken;
    }

}
