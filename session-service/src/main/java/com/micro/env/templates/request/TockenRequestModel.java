/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.templates.request;

/**
 *
 * @author onelove
 */
//@XmlRootElement
public class TockenRequestModel {

    private String token_type;
    private String access_token;
    private String id_token;
    private String expires_in;

    public TockenRequestModel() {
    }

    public TockenRequestModel(String token_type, String access_token, String id_token, String expires_in) {
        this.token_type = token_type;
        this.access_token = access_token;
        this.id_token = id_token;
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getId_token() {
        return id_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

}
