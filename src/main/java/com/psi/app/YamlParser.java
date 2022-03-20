package com.psi.app;

import com.psi.app.assertion.Assertion;
import com.psi.app.exceptions.IllegalClassDefinitionException;
import com.psi.app.exceptions.IllegalYamlParameterException;
import com.psi.app.exceptions.MissingYamlParameterException;
import com.psi.app.trigger.Trigger;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Scanner;

/**
 * Parser for yaml file. Gets input from given path, parses it with snakeyaml and creates a TestCase based on the
 * extracted parameters. Package for trigger classes and assertion classes have to be given, to retrieve the
 * corresponding class to a trigger / assertion id.
 */
public class YamlParser {

    private final String PATH_TRIGGER_PACKAGE = "com.psi.app.trigger";
    private final String PATH_ASSERTION_PACKAGE = "com.psi.app.assertion";

    public YamlParser() {}

    /**
     * Creates a TestCase from a given yaml file. Parses the input file with snakeyaml. Creates a new trigger with
     * information with the information from the "When" part of the file and a new assertion from the "Then"
     * part of the file.
     * @param yamlPath path of the yaml file
     * @return  test case with created trigger and assertion
     * @throws FileNotFoundException if there is no yaml file at given path
     */
    public TestCase getTestCaseFromYaml(String yamlPath) throws IOException {
        ParameterMap yamlData = getDataFromYaml(yamlPath);

        Map when = yamlData.tryGetMap("When");
        Trigger trigger = createTriggerFromYamlData(new ParameterMap(when));

        Map then = yamlData.tryGetMap("Then");
        Assertion assertion = createAssertionFromYamlData(new ParameterMap(then));

        return new TestCase(trigger, assertion);
    }

    /**
     * Given the data from the "When" part of the yaml file, create a new trigger. Choose the right trigger class
     * from the trigger id in the yaml file.
     * @param when "When" as ParameterMap
     * @return created trigger
     * @throws IllegalClassDefinitionException if no instance could be created of class corresponding to triggerId
     * @throws MissingYamlParameterException if an obligatory parameter is missing
     * @throws IllegalYamlParameterException if a parameter is illegal (wrong type or invalid value)
     */
    private Trigger createTriggerFromYamlData(ParameterMap when){
        String triggerId = when.tryGetString("trigger_id");
        Class triggerClass = getTriggerClassFromTriggerId(triggerId);
        try {
            Constructor triggerConstructor = triggerClass.getConstructor(ParameterMap.class);
            return (Trigger) triggerConstructor.newInstance(when);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                ExceptionInInitializerError e){
            throw new IllegalClassDefinitionException("Class "+triggerClass.getName()+" is wrongly defined, " +
                    "no instance could be created.");
        }
        catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if(targetException instanceof MissingYamlParameterException){
                throw (MissingYamlParameterException) targetException;
            }
            else if(targetException instanceof IllegalYamlParameterException){
                throw (IllegalYamlParameterException) targetException;
            }
            else {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    /**
     * Given the data from the "Then" part of the yaml file, create a new assertion. Choose the right trigger class
     * from the assertion id in the yaml file.
     * @param then "Then" as ParameterMap
     * @return created Assertion
     * @throws IllegalClassDefinitionException if no instance could be created of class corresponding to assertionId
     * @throws MissingYamlParameterException if an obligatory parameter is missing
     * @throws IllegalYamlParameterException if a parameter is illegal (wrong type or invalid value)
     */
    private Assertion createAssertionFromYamlData(ParameterMap then){
        String assertionId = then.tryGetString("assertion_id");
        Class assertionClass = getAssertionClassFromAssertionId(assertionId);
        try {
            Constructor assertionConstructor = assertionClass.getConstructor(ParameterMap.class);
            return (Assertion) assertionConstructor.newInstance(then);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException |
            ExceptionInInitializerError e){
            throw new IllegalClassDefinitionException("Class "+assertionClass.getName()+" is wrongly defined, " +
                    "no instance could be created.");
        }
        catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if(targetException instanceof MissingYamlParameterException){
                throw (MissingYamlParameterException) targetException;
            }
            else if(targetException instanceof IllegalYamlParameterException){
                throw (IllegalYamlParameterException) targetException;
            }
            else {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    /**
     * Constructs the name of the expected trigger class based on the given trigger id. Returns the class with the
     * constructed name, if it exists. Otherwise a IllegalYamlParameterException is thrown.
     * @param triggerId to get class name from
     * @return class corresponding to triggerId
     * @throws IllegalYamlParameterException if no corresponding trigger class was found for given triggerId
     */
    private Class getTriggerClassFromTriggerId(String triggerId){
        String triggerClassName = PATH_TRIGGER_PACKAGE+"."+triggerId.replaceAll("\\s+","")+"Trigger";
        try {
            return Class.forName(triggerClassName);
        }
        catch (ClassNotFoundException e){
            throw new IllegalYamlParameterException("Illegal trigger_id, no corresponding class found.");
        }
    }

    /**
     * Tries to parse the yaml file from the given path
     * @param assertionId to get class name from
     * @return class corresponding to assertionId
     * @throws IllegalYamlParameterException if no corresponding assertion class was found for given assertionId
     */
    private Class getAssertionClassFromAssertionId(String assertionId){
        String assertionClassName = PATH_ASSERTION_PACKAGE+"."+assertionId.replaceAll("\\s+","")+"Assertion";
        try {
            return Class.forName(assertionClassName);
        }
        catch (ClassNotFoundException e){
            throw new IllegalYamlParameterException("Illegal assertion_id, no corresponding class found.");
        }
    }

    /**
     * Parses the yaml file from the given path and returns the parsed information as a ParameterMap. First it is tried
     * to parse the given yaml with snakeyaml as is. If that's successful, the result is returned. If not, it is tried
     * to transform the given yaml file to a valid yaml file, since the syntax stated in the task description is invalid,
     * but should be a processable input. Syntax from the task description:
     *      When: File Created
     *          with_name: test.foo
     *          in_directory: execution
     *      Then: File Count
     *          after: 2s
     *          file_count: 0
     *          in_directory: execution
     * is transformed to:
     *      When:
     *          trigger_id: File Created
     *          with_name: test.foo
     *          in_directory: execution
     *      Then:
     *          assertion_id: File Count
     *          after: 2s
     *          file_count: 0
     *          in_directory: execution
     * @param yamlPath path of the input yaml file
     * @return a ParameterMap with the information extracted from the yaml life
     * @throws IOException if an error occurs while reading the input file
     * @throws org.yaml.snakeyaml.scanner.ScannerException if the file still cannot be parsed, after changing the syntax
     */
    private ParameterMap getDataFromYaml(String yamlPath) throws IOException {
        InputStream input = new FileInputStream(new File(yamlPath));
        try {
            ParameterMap parameters = new ParameterMap(new Yaml().load(input));
            return parameters;
        }
        catch (org.yaml.snakeyaml.scanner.ScannerException e) {
            Scanner scanner = new Scanner(new File(yamlPath));
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("When:")) {
                    String triggerId = line.substring(5).trim();
                    stringBuilder.append("When:\n  trigger_id: " + triggerId + "\n");
                } else if (line.startsWith("Then:")) {
                    String assertionId = line.substring(5).trim();
                    stringBuilder.append("Then:\n  assertion_id: " + assertionId + "\n");
                } else {
                    stringBuilder.append(line + "\n");
                }
            }
            ParameterMap parameters = new ParameterMap(new Yaml().load(stringBuilder.toString()));
            return parameters;
        }
    }
}
