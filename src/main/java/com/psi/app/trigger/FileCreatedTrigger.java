package com.psi.app.trigger;

import com.psi.app.ParameterMap;
import com.psi.app.exceptions.IllegalYamlParameterException;
import com.psi.app.exceptions.TriggerExecutionFailedException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Defines the trigger for trigger id "File Created". Uses two parameters, which are both obligatory: withName (respectively
 * with_name in YAML file) and inDirectory (respectively in_directory in YAML file).
 */
public class FileCreatedTrigger extends Trigger {

    private final String withName;
    private final String inDirectory;

    /**
     * Constructor for a new trigger with trigger id "File Created". Verifies passed parameters.
     * @param parameterMap are the parameters of the trigger extracted from the YAML file
     */
    public FileCreatedTrigger(ParameterMap parameterMap){
        this.withName = parameterMap.tryGetString("with_name");
        this.inDirectory = parameterMap.tryGetString("in_directory");

        verifyParameters();
    }

    /**
     * Executes the event as described by the parameters of the trigger, which were extracted from the yaml file. Creates
     * a new file called "withName" in directory "inDirectory".
     * @throws com.psi.app.exceptions.TriggerExecutionFailedException if an error occurred while executing the trigger.
     */
    @Override
    public void executeTrigger(){
        try {
            String fileName = inDirectory+"\\"+withName;
            System.out.println("FileCreatedTrigger: Creating new file \""+fileName+"\".");
            Path pathFile = Paths.get(fileName);
            Files.createFile(pathFile);
        } catch (IOException e) {
            throw new TriggerExecutionFailedException("FileCreatedTrigger could not be executed.",e);
        }
    }

    /**
     * Verifies if the parameter values extracted from the yaml file are valid. For this assertion "inDirectory" has to
     * be the path to an actual directory and there cannot already exist a file "withName" in the directory.
     * @throws com.psi.app.exceptions.IllegalYamlParameterException if a value is invalid.
     */
    @Override
    public void verifyParameters(){
        if(!new File(inDirectory).isDirectory()){
            throw new IllegalYamlParameterException("Illegal value for trigger parameter in_directory.");
        }
        if(new File(inDirectory+"\\"+withName).isFile()){
            throw new IllegalYamlParameterException("Illegal value for trigger parameters with_name and in_directory: " +
                    "File already exists.");
        }
    }

    String getWithName() {
        return withName;
    }

    String getInDirectory() {
        return inDirectory;
    }
}
