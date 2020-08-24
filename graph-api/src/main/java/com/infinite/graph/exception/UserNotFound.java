/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author onelove
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends Exception {

    public UserNotFound(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorMessage() {
        return this.getMessage();
    }

}
