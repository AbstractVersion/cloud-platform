/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gateway.api.conf.jwt;

/**
 *
 * @author onelove
 */
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.api.service.repository.SessionServiceRemoteRepository;
import com.gateway.api.service.repository.template.SessionInfo;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private SessionServiceRemoteRepository sessionRepo;
    private ObjectMapper mapper;

    public JwtUserDetailsService() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public SessionInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println(username);
            ResponseEntity obj = sessionRepo.findSessionById(username);
            if (obj.getStatusCode() == HttpStatus.OK) {
                return mapper.convertValue(obj.getBody(), SessionInfo.class);
            }
        } catch (Exception ex) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
