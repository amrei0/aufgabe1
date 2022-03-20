package com.psi.app.exceptions;

/**
 * Thrown, if a trigger or assertion class is wrongly defined and therefore creating a new instance of the class fails.
 */
public class IllegalClassDefinitionException extends RuntimeException  {

    public IllegalClassDefinitionException(String message){
        super(message);
    }
}
