/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.buisness;

import com.google.gson.JsonObject;
import com.infinite.graph.dataModel.MicrosoftTeamsUser;
import com.infinite.graph.exception.UserTokenExpiredException;
import com.infinite.graph.resource.template.request.UserToken;
import com.infinite.graph.service.Microsoft_User_Endpoint;
import com.infinite.graph.service.schema.Graph;
import com.microsoft.graph.models.extensions.User;
import java.net.UnknownHostException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author onelove
 */
@Service
public class GraphUserService extends Graph {

    @Autowired
    private Microsoft_User_Endpoint graph;

    private static String serviceUUID;

    static {
        serviceUUID = UUID.randomUUID().toString();
    }

    public GraphUserService() {
        super();
    }

    public MicrosoftTeamsUser getSignedUserInfo(UserToken tokenDetails) throws UserTokenExpiredException, UnknownHostException {
        MicrosoftTeamsUser user = new MicrosoftTeamsUser(this.graph.getUser(tokenDetails.getUserToken()), serviceUUID);
        return user;
    }

    public MicrosoftTeamsUser getUserInfo(UserToken tokenDetails, String userPrincipal) throws UserTokenExpiredException, UnknownHostException {
        MicrosoftTeamsUser user = new MicrosoftTeamsUser(this.graph.getUser(tokenDetails.getUserToken(), userPrincipal), serviceUUID);
        return user;
    }

}
