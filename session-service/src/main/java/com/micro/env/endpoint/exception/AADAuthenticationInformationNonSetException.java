package com.micro.env.endpoint.exception;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.micro.env.repository.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author onelove
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AADAuthenticationInformationNonSetException extends Exception {

    public AADAuthenticationInformationNonSetException(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorMessage() {
        return this.getMessage();
    }

}
