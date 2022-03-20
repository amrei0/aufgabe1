package com.psi.app.exceptions;

/**
 * Thrown, if an error occurs during checking an assertion
 */
public class AssertionExecutionFailedException extends RuntimeException {

    public AssertionExecutionFailedException(String message, Throwable cause) { super (message, cause); }
}
