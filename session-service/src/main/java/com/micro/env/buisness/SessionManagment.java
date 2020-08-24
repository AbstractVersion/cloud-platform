/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.buisness;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro.env.conf.JwtTokenUtil;
import com.micro.env.endpoint.exception.AADAuthenticationInformationNonSetException;
import com.micro.env.entity.SessionInfo;
import com.micro.env.repository.SessionRepository;
import com.micro.env.repository.exception.SessionAlreadyExists;
import com.micro.env.repository.remote.GraphServiceImpl;
import com.micro.env.repository.remote.dataModel.MicrosoftTeamsUser;
import com.micro.env.repository.remote.temlates.request.UserToken;
import com.micro.env.templates.request.TockenRequestModel;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author onelove
 */
@Service
public class SessionManagment {

    private static final Logger logger = Logger.getLogger(SessionManagment.class.getName());

    @Autowired
    private SessionRepository sessionRepo;
    @Autowired
    private GraphServiceImpl graphService;
    @Autowired
    private JwtTokenUtil tokeUtil;

    private ObjectMapper mapper;

    public SessionManagment() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public SessionInfo register_user_by_AAD_token(TockenRequestModel auth_model) throws AADAuthenticationInformationNonSetException, SessionAlreadyExists, JsonProcessingException {
        logger.info("Register Request has been triggered. Validation process.");
        if (auth_model == null || auth_model.getAccess_token().isEmpty()) {
            logger.log(Level.SEVERE, "The authentication informations where not included on the request.");
            throw new AADAuthenticationInformationNonSetException("The authentication informations where not found");
        }
        //        Requesting user information based on givven token
        // Microsoft token validation. If the token provided is valid,
        // Microsoft Graph will return the user's information.
        MicrosoftTeamsUser user = null;
        ResponseEntity obj = graphService.userInfo(new UserToken(auth_model.getAccess_token()));
        if (obj.getStatusCode() == HttpStatus.OK) {
            user = mapper.convertValue(obj.getBody(), MicrosoftTeamsUser.class);
        }

        return manageSession(user, auth_model);
    }

    private SessionInfo manageSession(MicrosoftTeamsUser user, TockenRequestModel auth_model) {
        //Generate Session
        SessionInfo session = new SessionInfo(
                user.getId().toString(),
                user.getDisplayName(),
                auth_model.getId_token(),
                auth_model.getAccess_token(),
                Integer.parseInt(auth_model.getExpires_in())
        );
        //register session on the database
        //each time user tries to register a session simpley overwrites the previus one
        this.sessionRepo.createSession(session);
        logger.log(Level.INFO, "User : {0} has been registerd to the database.", user.getDisplayName());
        //generate new selfSigned token && overwrite the session token
        session.setAccessToken(
                tokeUtil.generateToken(
                        session,
                        Integer.parseInt(auth_model.getExpires_in()) - 100
                )
        );
        //Overwrite the expiring time of the token 
        // That way the self signed token will always expire before the microsoft one
        //so as to trigger silent authentication on the client side
        session.setExpiring(Integer.parseInt(auth_model.getExpires_in()) - 100);
        return session;
    }
    
    //ANONYMOUS LOGIN REMOVE IN PRODUCTION

    public SessionInfo getAnonymousToken() {
        SessionInfo session = new SessionInfo(
                UUID.randomUUID().toString(),
                "Anonymous",
                "N/A",
                "N/A",
                32000
        );
        this.sessionRepo.createSession(session);
        session.setAccessToken(
                tokeUtil.generateToken(
                        session,
                        32000
                )
        );
        session.setExpiring(32000);
        return session;
    }

}
