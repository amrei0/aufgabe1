package com.psi.app.exceptions;

/**
 * Thrown, if an error occurs during execution of a trigger
 */
public class TriggerExecutionFailedException extends RuntimeException {

    public TriggerExecutionFailedException(String message, Throwable cause) { super (message, cause); }
}
