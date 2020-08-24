/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.service.schema;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;

/**
 *
 * @author onelove
 * 
 * This class overwirtes the default authentication procedure provided by Microsoft Graph SDK
 * 
 * source : https://docs.microsoft.com/en-us/graph/tutorials/java?tutorial-step=4
 */
public class MicrosoftGraphAuthenticationProvider implements IAuthenticationProvider {

    private String accessToken = null;

    public MicrosoftGraphAuthenticationProvider(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void authenticateRequest(IHttpRequest request) {
        // Add the access token in the Authorization header
        request.addHeader("Authorization", "Bearer " + accessToken);
    }

}
