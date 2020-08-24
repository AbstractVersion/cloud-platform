/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.repository.remote;

import com.micro.env.conf.FeignConfiguration;
import com.micro.env.repository.remote.temlates.request.UserToken;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author onelove
 */
@FeignClient(name = "graph-service", configuration = FeignConfiguration.class)
public interface GraphServiceImpl {

    final static String prefix = "/graph";

    @PostMapping(value = prefix + "/me", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> userInfo(@RequestBody UserToken tokenDetails);

    //POST /users/{id}
    //@Valid to validate the posted body attribute content
    @PostMapping(value = prefix + "/me/teams", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> list_user_teams(@RequestBody UserToken tokenDetails);

    @PostMapping(value = prefix + "team/{uuid}/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> list_team_members_resource(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails);

    @PostMapping(value = prefix + "team/{uuid}/sites", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> listTeamsSitesByTeamId(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails);
}
