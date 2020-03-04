/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.resource;

import com.infinite.graph.buisness.GraphUserService;
import com.infinite.graph.buisness.MicrosoftTeamsService;
import com.infinite.graph.exception.UserNotFound;
import com.infinite.graph.exception.UserTokenExpiredException;
import com.infinite.graph.resource.template.request.UserToken;
import java.net.UnknownHostException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author onelove
 */
@RestController
@RequestMapping("/graph")
public class GraphResource {

    @Autowired
    private MicrosoftTeamsService teams;
    @Autowired
    private GraphUserService user;

    @PostMapping(value = "/me", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> userInfo(@RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException, UnknownHostException {
        return ResponseEntity.ok(
                user.getSignedUserInfo(tokenDetails)
        );
    }

    //POST /users/{id}
    //@Valid to validate the posted body attribute content
    @PostMapping(value = "/me/teams", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> list_user_teams(@RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException {
        return ResponseEntity.ok(
                teams.findAllUserTeams(tokenDetails.getUserToken()
                )
        );
    }

    @PostMapping(value = "team/{uuid}/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> list_team_members_resource(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException {
        return ResponseEntity.ok(
                teams.list_team_members(tokenDetails.getUserToken(),
                        uuid
                )
        );
    }

    @PostMapping(value = "team/{uuid}/sites", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> listTeamsSitesByTeamId(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException {
        return ResponseEntity.ok(
                teams.list_team_sites(tokenDetails.getUserToken(), uuid)
        );
    }
}
