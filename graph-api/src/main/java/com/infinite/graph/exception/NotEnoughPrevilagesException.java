/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinite.graph.exception;

/**
 *
 * @author onelove
 */
public class NotEnoughPrevilagesException extends Exception {

    public NotEnoughPrevilagesException(String errorMessage) {
        super(errorMessage);
    }

    public String getErrorMessage() {
        return this.getMessage();
    }
}
