package com.psi.app.exceptions;

/**
 * Thrown, if yaml file is missing an obligatory parameter
 */
public class MissingYamlParameterException extends RuntimeException  {

    public MissingYamlParameterException(String message){
        super(message);
    }
}
