package com.psi.app.exceptions;

/**
 * Thrown, if a parameter of the yaml file has the wrong type or an invalid value.
 */
public class IllegalYamlParameterException extends RuntimeException  {

    public IllegalYamlParameterException(String message){
        super(message);
    }
}
