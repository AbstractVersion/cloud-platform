/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.resource.template.request;

/**
 *
 * @author onelove
 */
public class UserToken {

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
