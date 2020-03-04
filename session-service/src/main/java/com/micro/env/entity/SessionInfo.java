/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author onelove
 */
@Entity
@Table(name = "session_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SessionInfo.findAll", query = "SELECT s FROM SessionInfo s"),
    @NamedQuery(name = "SessionInfo.findById", query = "SELECT s FROM SessionInfo s WHERE s.id = :id"),
    @NamedQuery(name = "SessionInfo.findByExpiring", query = "SELECT s FROM SessionInfo s WHERE s.expiring = :expiring"),
    @NamedQuery(name = "SessionInfo.findByDateCreated", query = "SELECT s FROM SessionInfo s WHERE s.dateCreated = :dateCreated")})
public class SessionInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "token_id")
    private String tokenId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "access_token")
    private String accessToken;
    @Basic(optional = false)
    @NotNull
    @Column(name = "expiring")
    private int expiring;
    @Basic(optional = false)
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Transient
    private String selfToken;

    public String getSelfToken() {
        return selfToken;
    }

    public void setSelfToken(String selfToken) {
        this.selfToken = selfToken;
    }

    public SessionInfo() {
    }

    public SessionInfo(String id) {
        this.id = id;
    }

    public SessionInfo(String id, String tokenId, String accessToken, int expiring, Date dateCreated) {
        this.id = id;
        this.tokenId = tokenId;
        this.accessToken = accessToken;
        this.expiring = expiring;
        this.dateCreated = dateCreated;
    }

    public SessionInfo(String id, String tokenId, String accessToken, int expiring) {
        this.id = id;
        this.tokenId = tokenId;
        this.accessToken = accessToken;
        this.expiring = expiring;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiring() {
        return expiring;
    }

    public void setExpiring(int expiring) {
        this.expiring = expiring;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SessionInfo)) {
            return false;
        }
        SessionInfo other = (SessionInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.mavenproject1.SessionInfo[ id=" + id + " ]";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return getSelfToken();
    }

    @Override
    public String getUsername() {
        return getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
