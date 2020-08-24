package com.micro.env.repository.remote.dataModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.UUID;

/**
 *
 * @author onelove
 */
public class MicrosoftTeamsUser {

    private UUID id;
    private String displayName;
    private String givenName;
    private String jobTitle;
    private String mail;
    private String mobilePhone;
    private String preferredLanguage;
    private String userPrincipalName;
    private String surname;

    public MicrosoftTeamsUser() {
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
