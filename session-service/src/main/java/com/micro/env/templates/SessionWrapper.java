/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.templates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micro.env.entity.SessionInfo;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author onelove
 */
public class SessionWrapper implements UserDetails, Serializable {

    private String id;
    private String uname;

    public SessionWrapper() {

    }
    
    public SessionWrapper(SessionInfo info) {
        this.id = info.getId();
    }
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return uname;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

   
}
