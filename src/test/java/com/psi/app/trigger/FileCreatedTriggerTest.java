package com.psi.app.trigger;

import com.psi.app.YamlParser;
import com.psi.app.exceptions.IllegalYamlParameterException;
import com.psi.app.exceptions.MissingYamlParameterException;
import com.psi.app.exceptions.TriggerExecutionFailedException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileCreatedTrigger
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileCreatedTriggerTest {

    private static final String PATH_TEST_FILES = "src\\test\\resources\\yaml_test_files\\trigger\\file_created_trigger\\";
    private static final YamlParser yamlParser = new YamlParser();

    /**
     * Creates a trigger from a correct yaml file and asserts all variables were set correctly.
     * @throws FileNotFoundException can be thrown when loading the yaml file, but isn't expected.
     */
    @Test
    void test_creating_trigger_from_correct_yaml_should_result_in_correct_trigger() throws IOException {
        Trigger trigger = yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseExecuteTrigger.yaml").getTrigger();
        assertTrue(trigger instanceof FileCreatedTrigger);
        FileCreatedTrigger fileCreatedTrigger = (FileCreatedTrigger) trigger;
        assertEquals(fileCreatedTrigger.getWithName(),"test.foo");
        assertEquals(fileCreatedTrigger.getInDirectory(),"execution");
    }

    /**
     * Tests for missing parameters in yaml file. Try to create a FileCreatedTrigger by calling getTestCaseFromYaml,
     * but one parameter is missing in yaml file. Is expected to throw MissingYamlException with parameter-specific
     * error-message.
     */
    @Nested
    class TriggerParameterIsMissingTest{

        @Test
        void test_creating_trigger_when_trigger_id_is_missing_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseTriggerIdIsMissing.yaml"));
            assertEquals(e.getMessage(), "Parameter trigger_id is mandatory, but missing.");
        }

        @Test
        void test_creating_trigger_when_with_name_is_missing_should_result_in_exception() {
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseWithNameIsMissing.yaml"));
            assertEquals(e.getMessage(), "Parameter with_name is mandatory, but missing.");
        }

        @Test
        void test_creating_trigger_when_in_directory_is_missing_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsMissing.yaml"));
            assertEquals(e.getMessage(), "Parameter in_directory is mandatory, but missing.");
        }
    }

    /**
     * Tests for parameter values of a wrong type. Try to create a FileCreatedTrigger by calling getTestCaseFromYaml,
     * but one parameters value has the wrong type. Is expected to throw MissingYamlException with parameter-specific
     * error-message.
     */
    @Nested
    class TriggerParameterHasWrongTypeTest{

        @Test
        void test_creating_trigger_when_trigger_id_is_no_string_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseTriggerIdIsNoString.yaml"));
            assertEquals(e.getMessage(), "Illegal value for parameter trigger_id.");
        }

        @Test
        void test_creating_trigger_when_with_name_is_no_string_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseWithNameIsNoString.yaml"));
            assertEquals(e.getMessage(), "Illegal value for parameter with_name.");
        }

        @Test
        void test_creating_trigger_when_in_directory_is_no_string_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsNoString.yaml"));
            assertEquals(e.getMessage(), "Illegal value for parameter in_directory.");
        }
    }

    /**
     * Tests for invalid parameter values (but of the correct types). Try to create a FileCreatedTrigger by calling
     * getTestCaseFromYaml, but one parameters value is not valid. Is expected to throw MissingYamlException with
     * parameter-specific error-message.
    */
    @Nested
    class TriggerParameterHasInvalidValueTest{

        /**
         * trigger_id is invalid, if no corresponding trigger class exists.
         */
        @Test
        void test_creating_trigger_when_no_trigger_class_exists_for_trigger_id_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseNoTriggerClassExistsForTriggerId.yaml"));
            assertEquals(e.getMessage(), "Illegal trigger_id, no corresponding class found.");
        }

        /**
         * trigger_id is invalid, if there is no value for it in the yaml file.
         */
        @Test
        void test_creating_trigger_when_trigger_id_is_null_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseTriggerIdIsEmpty.yaml"));
            assertEquals(e.getMessage(), "Parameter trigger_id is mandatory, but missing.");
        }

        /**
         * with_name is invalid, if there is no value for it in the yaml file.
         */
        @Test
        void test_creating_trigger_when_with_name_is_null_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseWithNameIsEmpty.yaml"));
            assertEquals(e.getMessage(), "Parameter with_name is mandatory, but missing.");
        }

        /**
         * in_directory is invalid, if there is no value for it in the yaml file.
         */
        @Test
        void test_creating_trigger_when_in_directory_is_null_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsEmpty.yaml"));
            assertEquals(e.getMessage(), "Parameter in_directory is mandatory, but missing.");
        }

        /**
         * in_directory is invalid, if the directory doesn't exist.
         */
        @Test
        void test_creating_trigger_when_in_directory_is_no_directory_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsNoDirectory.yaml"));
            assertEquals(e.getMessage(), "Illegal value for trigger parameter in_directory.");
        }

    }

    /**
     * Test for when file "with_name" already exists in directory "in_directory".
     */
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class TriggerFileExistsTest{

        /**
         * Creates file "test.foo" in directory "execution" before test is executed.
         */
        @BeforeAll
        void setupFileExists(){
            Path pathFile = Paths.get("execution\\test.foo");
            try {
                Files.createFile(pathFile);
            } catch (IOException e) {
                throw new TriggerExecutionFailedException("FileCreatedTrigger could not be executed.",e);
            }
        }

        /**
         * Tries to create a FileCreatedTrigger by calling getTestCaseFromYaml, but a file with name "with_name" already
         * exists in directory "in_directory". Is expected to throw IllegalYamlParameterException with error-message
         * "Illegal value for trigger parameters with_name and in_directory: File already exists."
         */
        @Test
        void test_creating_trigger_when_file_already_exists_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseFileAlreadyExists.yaml"));
            assertEquals(e.getMessage(), "Illegal value for trigger parameters with_name and in_directory: " +
                    "File already exists.");
        }

        /**
         * Removes created file "test.foo" from directory "execution" after test is execution.
         */
        @AfterAll
        void cleanUpFileExists(){
            Path pathFile = Paths.get("execution\\test.foo");
            try {
                Files.delete(pathFile);
            } catch (IOException e) {
                throw new TriggerExecutionFailedException("FileCreatedTrigger could not be executed.",e);
            }
        }
    }

    /**
     * Tests the execution of FileCreatedTrigger
     */
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class TriggerExecutionTest{

        private FileCreatedTrigger fileCreatedTrigger;

        /**
         * Creates a trigger by calling getTestCaseFromYaml with a valid yaml file. Saves all files from directory
         * "execution" as filesPre before calling executeTrigger. After calling executeTrigger, again all files from
         * "execution" are retrieved and saved as filesPost. It is expected, that filesPost has one more file than
         * filesPre and the file defined by the FileCreatedTrigger is contained in filesPost, but not filesPre.
         * @throws FileNotFoundException  can be thrown when loading the yaml file, but isn't expected.
         */
        @Test
        void test_executing_trigger_should_result_in_created_file() throws IOException {
            Trigger trigger = yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseExecuteTrigger.yaml").getTrigger();
            assertTrue(trigger instanceof FileCreatedTrigger);
            fileCreatedTrigger = (FileCreatedTrigger) trigger;

            File dir = new File("execution");
            ArrayList<File> filesPre = new ArrayList<>(Arrays.asList(dir.listFiles()));
            fileCreatedTrigger.executeTrigger();
            ArrayList<File> filesPost = new ArrayList<>(Arrays.asList(dir.listFiles()));

            assertEquals(filesPre.size()+1,filesPost.size());
            assertFalse(filesPre.contains(
                    new File(fileCreatedTrigger.getInDirectory()+"\\"+fileCreatedTrigger.getWithName())));
            assertTrue(filesPost.contains(
                    new File(fileCreatedTrigger.getInDirectory()+"\\"+fileCreatedTrigger.getWithName())));
        }

        /**
         * Remove created file "with_name" from "in_directory"
         * @throws IOException can be thrown, when deleting, but isn't expected.
         */
        @AfterAll
        void cleanUpExecution() throws IOException {
            Path pathFile = Paths.get(fileCreatedTrigger.getInDirectory()+"\\"+fileCreatedTrigger.getWithName());
            Files.delete(pathFile);
        }
    }

}
