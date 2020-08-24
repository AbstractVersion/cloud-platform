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
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author onelove
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/graph")
public class GraphResource {

    @Autowired
    private MicrosoftTeamsService teams;
    @Autowired
    private GraphUserService user;

    @PostMapping(value = "/me-validate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> userInfo(HttpServletRequest req, HttpServletResponse res) throws UserNotFound, UserTokenExpiredException, UnknownHostException {
        log.info("Requesting user information based on token");
        return ResponseEntity.ok(
                user.getSignedUserInfo(new UserToken(req.getHeader("graph-token")))
        );
    }

    @PostMapping(value = "/me", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> userInfo(@RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException, UnknownHostException {
        log.info("Requesting user information based on token");
        return ResponseEntity.ok(
                user.getSignedUserInfo(tokenDetails)
        );
    }

    @PostMapping(value = "/user/{userPrincipal}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> userInfo(@RequestBody UserToken tokenDetails, @PathVariable String userPrincipal) throws UserNotFound, UserTokenExpiredException, UnknownHostException {
        log.info("Requesting user information based on token");
        return ResponseEntity.ok(
                user.getUserInfo(tokenDetails, userPrincipal)
        );
    }

    //POST /users/{id}
    //@Valid to validate the posted body attribute content
    @PostMapping(value = "/me/teams", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> list_user_teams(@RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException {
        log.info("Requesting user teams information based on token");
        return ResponseEntity.ok(
                teams.findAllUserTeams(tokenDetails.getUserToken()
                )
        );
    }

    @PostMapping(value = "/team/{uuid}/users", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> list_team_members_resource(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException {
        log.info("Requesting team : {} users", uuid);
        return ResponseEntity.ok(
                teams.list_team_members(tokenDetails.getUserToken(),
                        uuid
                )
        );
    }

    @PostMapping(value = "/team/{uuid}/sites", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> listTeamsSitesByTeamId(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException {
        log.info("Requesting team : {} site", uuid);
        return ResponseEntity.ok(
                teams.list_team_sites(tokenDetails.getUserToken(), uuid)
        );
    }

    @PostMapping(value = "team/{uuid}/user/{userId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addUserToMicrosoftTeam(@PathVariable UUID uuid, @PathVariable UUID userId, @RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException, IOException {
        log.info("Requesting team : {} site", uuid);
        return ResponseEntity.ok(
                teams.addUserToTeam(tokenDetails.getUserToken(), uuid.toString(), userId.toString())
        );
    }

    @DeleteMapping(value = "team/{uuid}/user/{userId}", consumes = "application/json")
    public ResponseEntity<?> deleteUserFromTeam(@PathVariable UUID uuid, @PathVariable UUID userId, @RequestBody UserToken tokenDetails) throws UserNotFound, UserTokenExpiredException, IOException {
        log.info("Removing user {} from Microsoft team : {} site", uuid, userId);
        return ResponseEntity.ok(
                teams.deleteUserFromTeam(tokenDetails.getUserToken(), uuid, userId)
        );
    }

}
