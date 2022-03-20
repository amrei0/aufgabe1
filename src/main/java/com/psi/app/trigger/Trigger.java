package com.psi.app.trigger;

/**
 * Abstract class describing a trigger of a testcase. For every trigger id in use, an inheriting class is expected
 * to be implemented.
 */
public abstract class Trigger {

    /**
     * Executes the event as described by the parameters of the trigger, which were extracted from the yaml file.
     * @throws com.psi.app.exceptions.TriggerExecutionFailedException if an error occurred while executing the trigger.
     */
    public abstract void executeTrigger();

    /**
     * Verifies if the parameter values extracted from the yaml file are valid.
     * @throws com.psi.app.exceptions.IllegalYamlParameterException if a value is invalid.
     */
    public abstract void verifyParameters();
}
