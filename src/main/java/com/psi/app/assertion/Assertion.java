package com.psi.app.assertion;

/**
 * Abstract class describing an assertion of a testcase. For every assertion id in use, an inheriting class is expected
 * to be implemented.
 */
public abstract class Assertion {

    /**
     * Checks, whether the assertion is fulfilled based on the parameters, which were extracted from the yaml file.
     * @return true, if the assertion is fulfilled, otherwise return false.
     * @throws com.psi.app.exceptions.AssertionExecutionFailedException if an error occurred while checking the assertion.
     */
    public abstract boolean checkAssertion();

    /**
    * Verifies if the parameter values extracted from the yaml file are valid.
    * @throws com.psi.app.exceptions.IllegalYamlParameterException if a value is invalid.
    */
    protected abstract void verifyParameters();
}
