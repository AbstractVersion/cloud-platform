/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.buisness;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.infinite.graph.dataModel.MicrosoftSite;
import com.infinite.graph.dataModel.MicrosoftTeamsModel;
import com.infinite.graph.dataModel.MicrosoftTeamsUser;
import com.infinite.graph.exception.UserTokenExpiredException;
import com.infinite.graph.service.Teams_Endpoint;
import com.microsoft.graph.models.extensions.Site;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author onelove
 */
@Service
public class MicrosoftTeamsService {

    private Gson jsonfy = new Gson();

    @Autowired
    private Teams_Endpoint teams_endpoint;

    public List<MicrosoftTeamsModel> findAllUserTeams(String accessToken) throws UserTokenExpiredException {
        return castGroupToMicrosoftTeamsList(teams_endpoint.listUseTeams(accessToken).getAsJsonArray("value").deepCopy());
    }

    private List<MicrosoftTeamsModel> castGroupToMicrosoftTeamsList(JsonArray array) {
        List<MicrosoftTeamsModel> temp = new ArrayList<>();
        for (JsonElement model : array) {
            temp.add(jsonfy.fromJson(model, MicrosoftTeamsModel.class));
        }
        return temp;
    }

    private List<?> castData(JsonArray array, Class<?> class_type) {
        List temp = new ArrayList<>();
        for (JsonElement model : array) {
            temp.add(jsonfy.fromJson(model, class_type));
        }
        return temp;
    }


    public List<?> list_team_members(String accessToken, UUID team_id) throws UserTokenExpiredException {
        return castData(teams_endpoint.listTeamUsers(accessToken, team_id).getAsJsonArray("value").deepCopy(), MicrosoftTeamsUser.class);
    }

    public Object list_team_sites(String accessToken, UUID team_id) throws UserTokenExpiredException {
        return teams_endpoint.findGroupCoprespondingSite(accessToken, team_id);
    }

}
