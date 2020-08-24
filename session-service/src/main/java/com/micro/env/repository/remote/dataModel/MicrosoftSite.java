package com.micro.env.repository.remote.dataModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Date;

/**
 *
 * @author onelove
 */
public class MicrosoftSite {

    private Date createdDateTime;
    private String description;
    private String id;
    private Date lastModifiedDateTime;
    private String name;
    private String webUrl;
    private String displayName;

    public MicrosoftSite() {
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public Date getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public String getName() {
        return name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
