package com.micro.env.repository.exception;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author onelove
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SessionAlreadyExists extends Exception {

    public SessionAlreadyExists(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorMessage() {
        return this.getMessage();
    }

}
