/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.dataModel;

import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.Group;
import java.util.UUID;

/**
 *
 * @author onelove
 */
public class MicrosoftTeamsModel {

    private UUID id;
    private String displayName;
    private String description;
    private boolean isArchived;

    public MicrosoftTeamsModel() {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    
    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isIsArchived() {
        return isArchived;
    }
    
    
}
