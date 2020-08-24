/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.staticInfo;

/**
 *
 * @author onelove
 */
public class URLCollection {

    private final static String baseGraphURL = "https://graph.microsoft.com/v1.0";
    private final static String baseGroupURL = "/groups/";
    private final static String rootSiteURL = "/sites/root";

    public static String generateTeamToSiteURL(String uuid) {
        return new StringBuilder().append(baseGraphURL).append(baseGroupURL).append(uuid).append(rootSiteURL).toString();
    }

}
