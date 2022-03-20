package com.psi.app;

import com.psi.app.exceptions.IllegalYamlParameterException;
import com.psi.app.exceptions.MissingYamlParameterException;

import java.util.Map;

/**
 * Holds a map, where the type of the values is unknown. Offers methods to get a specific value from the map, only if
 * it has the expected type.
 */
public class ParameterMap {

    private final Map parameters;

    /**
     * Creates a new ParameterMap with a given Map
     * @param parameters given map
     */
    ParameterMap(Map parameters){
        this.parameters = parameters;
    }

    /**
     * Returns the value mapped to the given key, if one exists and is an instance of class cls. Otherwise an
     * Exception is thrown.
     * @param key of the wanted value
     * @param cls expected class of value
     * @return value as an Object
     * @throws MissingYamlParameterException if there is no entry for the given key
     * @throws IllegalYamlParameterException if the value of the key has an invalid type
     */
    private Object tryGet(String key, Class cls){
        Object value = parameters.get(key);
        if(value==null){
            throw new MissingYamlParameterException("Parameter "+key+" is mandatory, but missing.");
        }
        else if(cls.isInstance(value)){
            return value;
        }
        else {
            throw new IllegalYamlParameterException("Illegal value for parameter "+key+".");
        }
    }

    /**
     * Returns the value mapped to the given key, if one exists and is an instance of Integer. Otherwise an
     * Exception is thrown.
     * @param key of the wanted value
     * @return value as an int
     */
    public int tryGetInt(String key){
        return (int) tryGet(key, Integer.class);
    }

    /**
     * Returns the value mapped to the given key, if one exists and is an instance of String. Otherwise an
     * Exception is thrown.
     * @param key of the wanted value
     * @return value as a String
     */
    public String tryGetString(String key){
        return (String) tryGet(key, String.class);
    }

    /**
     * Returns the value mapped to the given key, if one exists and is an instance of Map. Otherwise an
     * Exception is thrown.
     * @param key of the wanted value
     * @return value as a Map
     */
    Map tryGetMap(String key){
        return (Map) tryGet(key, Map.class);
    }
}
