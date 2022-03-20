package com.psi.app.assertion;

import com.psi.app.ParameterMap;
import com.psi.app.exceptions.AssertionExecutionFailedException;
import com.psi.app.exceptions.IllegalYamlParameterException;
import com.psi.app.exceptions.MissingYamlParameterException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Assertion for assertion id "File Count". Waits for "after" seconds and then checks, whether exactly "file_count"
 * documents exist in "in_directory". "after" is not mandatory, if "after" is not listed in yaml file, it is set to
 * 0 seconds. "file_count" and "in_directory" are mandatory, a MissingYamlParameterException is thrown.
 */
public class FileCountAssertion extends Assertion {

    private long after; // number of seconds to wait before checking the assertion
    private final int fileCount;
    private final String inDirectory;

    /**
     * Creates a new FileCountAssertion based on the given ParameterMap. If "after" is missing, the corresponding
     * MissingYamlParameterException is catched and after is set to 0. Afterwards verifies, if parameter values are
     * valid
     * @param parameterMap are the parameters of the assertion extracted from the YAML file
     */
    public FileCountAssertion(ParameterMap parameterMap){
        try {
            this.after = parseSecondsFromString(parameterMap.tryGetString("after"));
        }
        catch (MissingYamlParameterException e){
            this.after = 0;
        }
        this.fileCount = parameterMap.tryGetInt("file_count");
        this.inDirectory = parameterMap.tryGetString("in_directory");

        verifyParameters();
    }

    /**
     * Parses String "after" from yaml file to corresponding number of seconds.
     * @param after as extracted from yaml file
     * @return number of seconds represented by after
     */
    private long parseSecondsFromString(String after){
        Duration duration = Duration.parse("PT"+after.replaceAll("\\s+", ""));
        return duration.getSeconds();
    }

    /**
     * Checks, whether the assertion is fulfilled based on the parameters, which were extracted from the yaml file.
     * Assertion is fulfilled, if after "after" seconds there are exactly "file_count" documents in "in_directory".
     * @return true, if the assertion is fulfilled, otherwise return false.
     * @throws com.psi.app.exceptions.AssertionExecutionFailedException if an error occurred while checking the assertion.
     */
    @Override
    public boolean checkAssertion() {
        try {
            System.out.println("FileCountAssertion: Start sleeping "+after+" seconds. Expecting "+fileCount+
                    " files in directory \""+inDirectory+"\".");
            TimeUnit.SECONDS.sleep(after);
            Stream<Path> streamDirectory = Files.list(Paths.get(inDirectory));
            long filesInDirectory = streamDirectory.count();
            return filesInDirectory==fileCount;
        } catch (InterruptedException | IOException e) {
            throw new AssertionExecutionFailedException("Checking FileCountAssertion failed.",e);
        }
    }

    /**
     * Verifies if the parameter values extracted from the yaml file are valid. For this assertion "after" and "fileCount"
     * have to be at least 0 and "inDirectory" has to be the path to an actual directory.
     * @throws com.psi.app.exceptions.IllegalYamlParameterException if a value is invalid.
     */
    @Override
    protected void verifyParameters() {
        if(after<0){
            throw new IllegalYamlParameterException("Illegal value for assertion parameter after.");
        }
        if(fileCount<0){
            throw new IllegalYamlParameterException("Illegal value for assertion parameter file_count.");
        }
        if(!new File(inDirectory).isDirectory()){
            throw new IllegalYamlParameterException("Illegal value for assertion parameter in_directory.");
        }
    }

    long getAfter() {
        return after;
    }

    int getFileCount() {
        return fileCount;
    }

    String getInDirectory() {
        return inDirectory;
    }
}
