package com.psi.app;

import com.psi.app.assertion.Assertion;
import com.psi.app.trigger.Trigger;

/**
 * Represents a test case from a yaml file, which consists of a trigger and an assertion.
 */
public class TestCase {

    private final Trigger trigger;
    private final Assertion assertion;

    TestCase(Trigger trigger, Assertion assertion){
        this.trigger = trigger;
        this.assertion = assertion;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Assertion getAssertion() {
        return assertion;
    }
}
