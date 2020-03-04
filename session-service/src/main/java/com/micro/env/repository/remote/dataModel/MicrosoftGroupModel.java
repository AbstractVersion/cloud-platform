package com.micro.env.repository.remote.dataModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author onelove
 */
public class MicrosoftGroupModel {

    private UUID id;
    private Date deletedDateTime;
    private Date createdDateTime;
    private String description;
    private String displayName;
    private List<String> groupTypes;
    private String mail;
    private boolean mailEnabled;
    private String mailNickname;
    private String preferredDataLocation;
    private List proxyAddresses;
    private Date renewedDateTime;
    private String visibility;

    public MicrosoftGroupModel() {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDeletedDateTime(Date deletedDateTime) {
        this.deletedDateTime = deletedDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGroupTypes(List<String> groupTypes) {
        this.groupTypes = groupTypes;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMailEnabled(boolean mailEnabled) {
        this.mailEnabled = mailEnabled;
    }

    public void setMailNickname(String mailNickname) {
        this.mailNickname = mailNickname;
    }

    public void setPreferredDataLocation(String preferredDataLocation) {
        this.preferredDataLocation = preferredDataLocation;
    }

    public void setProxyAddresses(List<String> proxyAddresses) {
        this.proxyAddresses = proxyAddresses;
    }

    public void setRenewedDateTime(Date renewedDateTime) {
        this.renewedDateTime = renewedDateTime;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public UUID getId() {
        return id;
    }

    public Date getDeletedDateTime() {
        return deletedDateTime;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getGroupTypes() {
        return groupTypes;
    }

    public String getMail() {
        return mail;
    }

    public boolean isMailEnabled() {
        return mailEnabled;
    }

    public String getMailNickname() {
        return mailNickname;
    }

    public String getPreferredDataLocation() {
        return preferredDataLocation;
    }

    public List<String> getProxyAddresses() {
        return proxyAddresses;
    }

    public Date getRenewedDateTime() {
        return renewedDateTime;
    }

    public String getVisibility() {
        return visibility;
    }

}
