/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author onelove
 */
@Slf4j
@Service
public class Teams_Endpoint extends Graph {

    @Autowired
    private Graph_Handler graphCustomHttpClient;

    public Teams_Endpoint() {
        super();
    }

    public List<Group> listUserGrooups(String accessToken) throws UserTokenExpiredException {
        try {
            log.debug("Requesting user teams information...");
            ensureGraphClient(accessToken);
            // GET /me to get authenticated user
            this.graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

            IGroupCollectionPage groups = graphClient.groups()
                    .buildRequest()
                    .get();
            return groups.getCurrentPage();
        } catch (com.microsoft.graph.http.GraphServiceException ex) {
            if (ex.getResponseCode() == 401) {
                log.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return null;
        }
    }

    public boolean addUserToTeam(String accessToken, String teamId, String userId) throws UserTokenExpiredException, MalformedURLException, IOException {
        try {
            JSONObject obj = new JSONObject();
            log.info("Registering user : {} to team : {}.", userId, teamId);
            obj.put("@odata.id", "https://graph.microsoft.com/v1.0/directoryObjects/" + userId);

            // Sending get request
            log.info(obj.toJSONString());
            String jsonInputString = obj.toJSONString();
            URL url = new URL("https://graph.microsoft.com/v1.0/groups/" + teamId + "/members/$ref");
            log.info("one");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            log.info("tow");

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            log.info("three");
            log.info(jsonInputString);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;

            StringBuffer response = new StringBuffer();
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            return true;
        } catch (com.microsoft.graph.http.GraphServiceException ex) {
            if (ex.getResponseCode() == 401) {
                log.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return false;
        }
    }

    public JsonObject listUseTeams(String accessToken) throws UserTokenExpiredException {
        try {
            log.debug("Requesting user teams information...");
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
                log.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return null;
        }
    }

    public JsonObject listTeamUsers(String accessToken, UUID team_id) throws UserTokenExpiredException {
        try {
            log.debug("Requesting team members for team : " + team_id + "information...");
            ensureGraphClient(accessToken);
            // GET /me to get authenticated user
            this.graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

            IDirectoryObjectCollectionWithReferencesPage members = graphClient.groups(team_id.toString()).members()
                    .buildRequest()
                    .get();
            return members.getRawObject();
        } catch (com.microsoft.graph.http.GraphServiceException ex) {
            if (ex.getResponseCode() == 401) {
                log.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return null;
        }
    }

    public boolean deleteUserFromteam(String accessToken, UUID team_id, UUID userId) throws UserTokenExpiredException {
        try {
            log.debug("Requesting team members for team : " + team_id + "information...");
            ensureGraphClient(accessToken);
            // GET /me to get authenticated user
            this.graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
            IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

            graphClient.groups(team_id.toString()).members(userId.toString()).reference()
                    .buildRequest()
                    .delete();
            return true;
        } catch (com.microsoft.graph.http.GraphServiceException ex) {
            if (ex.getResponseCode() == 401) {
                log.error("User token has been expired");
                throw new UserTokenExpiredException("User token has been expired");
            }
            return false;
        }
    }

    public Object findGroupCoprespondingSite(String accessToken, UUID groupID) throws UserTokenExpiredException {
        return this.graphCustomHttpClient.requestRESTInformationGET(
                generateTeamToSiteURL(groupID.toString()),
                accessToken,
                MicrosoftSite.class);
    }

}
