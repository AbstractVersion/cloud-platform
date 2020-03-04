/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.dataModel;

import com.google.gson.JsonObject;
import com.microsoft.graph.models.extensions.User;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 *
 * @author onelove
 */
public class MicrosoftTeamsUser extends ServiceReplyModel {

    private UUID id;
    private String displayName;
    private String givenName;
    private String jobTitle;
    private String mail;
    private String mobilePhone;
    private String preferredLanguage;
    private String userPrincipalName;
    private String surname;

    public MicrosoftTeamsUser(User object) {
        super(null);
        this.id = UUID.fromString(object.id);
        this.displayName = object.displayName;
        this.givenName = object.givenName;
        this.jobTitle = object.jobTitle;
        this.mail = object.mail;
        this.mobilePhone = object.mobilePhone;
        this.preferredLanguage = object.preferredLanguage;
        this.userPrincipalName = object.userPrincipalName;
        this.surname = object.surname;
    }

    public MicrosoftTeamsUser(User object, String serviceId) throws UnknownHostException {
        super(serviceId, InetAddress.getLocalHost().getHostName());
        this.id = UUID.fromString(object.id);
        this.displayName = object.displayName;
        this.givenName = object.givenName;
        this.jobTitle = object.jobTitle;
        this.mail = object.mail;
        this.mobilePhone = object.mobilePhone;
        this.preferredLanguage = object.preferredLanguage;
        this.userPrincipalName = object.userPrincipalName;
        this.surname = object.surname;
    }

    public MicrosoftTeamsUser(User object, String serviceId, String host) {
        super(serviceId, host);
        this.id = UUID.fromString(object.id);
        this.displayName = object.displayName;
        this.givenName = object.givenName;
        this.jobTitle = object.jobTitle;
        this.mail = object.mail;
        this.mobilePhone = object.mobilePhone;
        this.preferredLanguage = object.preferredLanguage;
        this.userPrincipalName = object.userPrincipalName;
        this.surname = object.surname;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getMail() {
        return mail;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public String getSurname() {
        return surname;
    }

}
