/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gateway.api.service.repository;

import com.gateway.api.service.repository.conf.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author onelove
 */
@FeignClient(name = "session-service", configuration = FeignConfiguration.class)
public interface SessionServiceRemoteRepository {

    //retrieveSpecific
    @RequestMapping(method = RequestMethod.GET, path = "/session/{uuid}", produces = "application/json")
    public ResponseEntity<?> findSessionById(@PathVariable String uuid);
}
