/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.micro.env.buisness.SessionManagment;
import com.micro.env.endpoint.exception.AADAuthenticationInformationNonSetException;
import com.micro.env.entity.SessionInfo;
import com.micro.env.repository.SessionRepository;
import com.micro.env.repository.exception.SessionAlreadyExists;
import com.micro.env.repository.exception.SessionNotFound;
import com.micro.env.templates.request.TockenRequestModel;
import com.micro.env.templates.response.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author onelove
 */
@RestController
@RequestMapping("/session")
public class SessionEndpoint {

    @Autowired
    private SessionRepository sessionRepo;

    @Autowired
    private SessionManagment sessionService;

    //retrieveAllSessions
    @RequestMapping(method = RequestMethod.GET, path = "/", produces = "application/json")
    public ResponseEntity<?> findAllSessions() {
        return ResponseEntity.ok(sessionRepo.findSessions());
    }

    //retrieveSpecific
    @RequestMapping(method = RequestMethod.GET, path = "/{uuid}", produces = "application/json")
    public ResponseEntity<?> findSessionById(@PathVariable String uuid) throws SessionNotFound {
        return ResponseEntity.ok(sessionRepo.findSession(uuid));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> createSession(@RequestBody TockenRequestModel session) throws SessionAlreadyExists, AADAuthenticationInformationNonSetException, JsonProcessingException {
        return ResponseEntity.ok(
                sessionService.register_user_by_AAD_token(session)
        );
    }
}
