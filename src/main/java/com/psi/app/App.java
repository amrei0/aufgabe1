package com.psi.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class App {

    /**
     * Creates test case from given yaml file, executes trigger and checks assertion afterwards.
     * @param args is expected to contain the path of the yaml file
     * @throws IOException is tan error occurred while reading the yaml file
     * @throws FileNotFoundException if no path for the yaml file was submitted
     */
    public static void main( String[] args ) throws IOException{
        if(args.length<1) throw new FileNotFoundException("No path to YAML file submitted");
        setUpDirectory();
        YamlParser yamlParser = new YamlParser();
        TestCase testCase;
        testCase = yamlParser.getTestCaseFromYaml(args[0]);
        testCase.getTrigger().executeTrigger();
        System.out.println("Result assertion: "+testCase.getAssertion().checkAssertion());
        cleanUp();
    }

    /**
     * Adds directory "execution" to root if it doesn't already exist. Is used by tests and existing yaml test cases.
     */
    private static void setUpDirectory(){
        new File("execution").mkdir();
    }

    /**
     * Deletes all files from directory "execution".
     * @throws IOException if the directory was not found
     */
    private static void cleanUp() throws IOException {
        try {
            File dir = new File("execution");
            File[] files = dir.listFiles();
            if(files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            System.out.println("Cleaned up directory \"execution\".");
        }
        catch(NullPointerException e){
            throw new IOException("Directory not found in cleanUp");
        }
    }

}
