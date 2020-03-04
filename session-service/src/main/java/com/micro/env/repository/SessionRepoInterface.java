/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.repository;

import com.micro.env.entity.SessionInfo;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author onelove
 */
public interface SessionRepoInterface extends CrudRepository<SessionInfo, String> {
    
}
