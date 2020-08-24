/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gateway.api.service.repository;

import com.gateway.api.service.repository.conf.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;

/**
 *
 * @author onelove
 */
@FeignClient(name = "graph-service", configuration = FeignConfiguration.class)
public interface GraphServiceRemoteRepository {
    
}
