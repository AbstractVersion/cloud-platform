/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.service;

import com.google.gson.JsonObject;
import com.infinite.graph.dataModel.MicrosoftSite;
import com.infinite.graph.exception.UserTokenExpiredException;
import com.infinite.graph.service.custom.httpClient.Graph_Handler;
import com.infinite.graph.service.schema.Graph;
import static com.infinite.graph.staticInfo.URLCollection.generateTeamToSiteURL;
import com.microsoft.graph.models.extensions.Group;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.Site;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IDirectoryObjectCollectionPage;
import com.microsoft.graph.requests.extensions.IDirectoryObjectCollectionWithReferencesPage;
import com.microsoft.graph.requests.extensions.IGroupCollectionPage;
import com.microsoft.graph.requests.extensions.ISiteCollectionPage;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author onelove
 */
@Service
public class Teams_Endpoint extends Graph {

    @Autowired
    private Graph_Handler graphCustomHttpClient;

    public Teams_Endpoint() {
        super();
    }

    public List<Group> listUserGrooups(String accessToken) throws UserTokenExpiredException {
        try {
            logger.debug("Requesting user teams information...");
            ensureGraphClient(accessToken);
            // GET /me to get authenticated user
            this.graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

            IGroupCollectionPage groups = graphClient.groups()
                    .buildRequest()
                    .get();
            return groups.getCurrentPage();
        } catch (com.microsoft.graph.http.GraphServiceException ex) {
            if (ex.getResponseCode() == 401) {
                logger.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return null;
        }
    }

    public JsonObject listUseTeams(String accessToken) throws UserTokenExpiredException {
        try {
            logger.debug("Requesting user teams information...");
            ensureGraphClient(accessToken);
            // GET /me to get authenticated user
            this.graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

            IGroupCollectionPage joinedTeams = graphClient.me().joinedTeams()
                    .buildRequest()
                    .get();
            System.out.println(joinedTeams.getRawObject());
            return joinedTeams.getRawObject();
        } catch (com.microsoft.graph.http.GraphServiceException ex) {
            if (ex.getResponseCode() == 401) {
                logger.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return null;
        }
    }

    public JsonObject listTeamUsers(String accessToken, UUID team_id) throws UserTokenExpiredException {
        try {
            logger.debug("Requesting team members for team : " + team_id + "information...");
            ensureGraphClient(accessToken);
            // GET /me to get authenticated user
            this.graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

            IDirectoryObjectCollectionWithReferencesPage members = graphClient.groups(team_id.toString()).members()
                    .buildRequest()
                    .get();
            return members.getRawObject();
        } catch (com.microsoft.graph.http.GraphServiceException ex) {
            if (ex.getResponseCode() == 401) {
                logger.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return null;
        }
    }

    public Object findGroupCoprespondingSite(String accessToken, UUID groupID) throws UserTokenExpiredException {
        return this.graphCustomHttpClient.requestRESTInformationGET(
                generateTeamToSiteURL(groupID.toString()),
                accessToken,
                MicrosoftSite.class);
    }

}
