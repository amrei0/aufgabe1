package com.psi.app.assertion;

import com.psi.app.YamlParser;
import com.psi.app.exceptions.IllegalYamlParameterException;
import com.psi.app.exceptions.MissingYamlParameterException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FileCountAssertion
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FileCountAssertionTest {

    private static final String PATH_TEST_FILES = "src\\test\\resources\\yaml_test_files\\assertion\\file_count_assertion\\";
    private static final YamlParser yamlParser = new YamlParser();

    /**
     * Adds directory "execution" to root if it doesn't already exist. Is used by tests and existing yaml test cases.
     */
    @BeforeAll
    static void setUpDirectory(){
        new File("execution").mkdir();
    }

    /**
     * Creates an assertion from a correct yaml file and asserts all variables were set correctly.
     * @throws FileNotFoundException can be thrown when loading the yaml file, but isn't expected.
     */
    @Test
    void test_creating_assertion_from_correct_yaml_should_result_in_correct_assertion() throws IOException {
        Assertion assertion = yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseAssertionFulfilled.yaml").getAssertion();
        assertTrue(assertion instanceof FileCountAssertion);
        FileCountAssertion fileCountAssertion = (FileCountAssertion) assertion;
        assertEquals(fileCountAssertion.getAfter(),2);
        assertEquals(fileCountAssertion.getFileCount(),0);
        assertEquals(fileCountAssertion.getInDirectory(),"execution");
    }

    /**
     * Tests for missing parameters in yaml file. Try to create a FileCountAssertion by calling getTestCaseFromYaml,
     * but one parameter is missing in yaml file. Is expected to throw MissingYamlException with parameter-specific
     * error-message, expect for missing after.
     */
    @Nested
    class AssertionParameterIsMissingTest {
        @Test
        void test_creating_assertion_when_assertion_id_is_missing_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseAssertionIdIsMissing.yaml"));
            assertEquals(e.getMessage(), "Parameter assertion_id is mandatory, but missing.");
        }

        /**
         * No exception expected, if after is missing, it is expected to be set to 0.
         * @throws FileNotFoundException can be thrown when loading the yaml file, but isn't expected.
         */
        @Test
        void test_creating_assertion_when_after_is_missing_should_result_in_after_is_0() throws IOException {
            FileCountAssertion fileCountAssertion = (FileCountAssertion) yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +
                    "testCaseAfterIsMissing.yaml").getAssertion();
            assertEquals(fileCountAssertion.getAfter(),0);
        }

        @Test
        void test_creating_assertion_when_file_count_is_missing_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseFileCountIsMissing.yaml"));
            assertEquals(e.getMessage(), "Parameter file_count is mandatory, but missing.");
        }

        @Test
        void test_creating_assertion_when_in_directory_is_missing_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsMissing.yaml"));
            assertEquals(e.getMessage(), "Parameter in_directory is mandatory, but missing.");
        }
    }

    /**
     * Tests for parameter values of a wrong type. Try to create a FileCountAssertion by calling getTestCaseFromYaml,
     * but one parameters value has the wrong type. Is expected to throw MissingYamlException with parameter-specific
     * error-message.
     */
    @Nested
    class AssertionParameterHasWrongTypeTest{
        @Test
        void test_creating_assertion_when_assertion_id_is_no_string_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseAssertionIdIsNoString.yaml"));
            assertEquals(e.getMessage(), "Illegal value for parameter assertion_id.");
        }

        @Test
        void test_creating_assertion_when_after_is_no_string_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseAfterIsNoString.yaml"));
            assertEquals(e.getMessage(), "Illegal value for parameter after.");
        }

        @Test
        void test_creating_assertion_when_file_count_is_no_int_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseFileCountIsNoInt.yaml"));
            assertEquals(e.getMessage(), "Illegal value for parameter file_count.");
        }

        @Test
        void test_creating_assertion_when_in_directory_is_no_string_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsNoString.yaml"));
            assertEquals(e.getMessage(), "Illegal value for parameter in_directory.");
        }
    }

    /**
     * Tests for invalid parameter values (but of the correct types). Try to create a FileCont by calling
     * getTestCaseFromYaml, but one parameters value is not valid. Is expected to throw MissingYamlException with
     * parameter-specific error-message.
     */
    @Nested
    class AssertionParameterHasInvalidValueTest{

        /**
         * assertion_id is invalid, if no corresponding assertion class exists.
         */
        @Test
        void test_creating_assertion_when_no_assertion_class_exists_for_assertion_id_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseNoAssertionClassExistsForAssertionId.yaml"));
            assertEquals(e.getMessage(), "Illegal assertion_id, no corresponding class found.");
        }

        /**
         * assertion_id is invalid, if there is no value for it in the yaml file.
         */
        @Test
        void test_creating_assertion_when_assertion_id_is_empty_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseAssertionIdIsEmpty.yaml"));
            assertEquals(e.getMessage(), "Parameter assertion_id is mandatory, but missing.");
        }

        /**
         * after is invalid, if it is negative.
         */
        @Test
        void test_creating_assertion_when_after_is_negative_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseAfterIsNegative.yaml"));
            assertEquals(e.getMessage(), "Illegal value for assertion parameter after.");
        }

        /**
         * file_count is invalid, if it is negative
         */
        @Test
        void test_creating_assertion_when_file_count_is_negative_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseFileCountIsNegative.yaml"));
            assertEquals(e.getMessage(), "Illegal value for assertion parameter file_count.");
        }

        /**
         * in_directory is invalid, if the directory doesn't exist.
         */
        @Test
        void test_creating_assertion_when_in_directory_is_empty_should_result_in_exception(){
            MissingYamlParameterException e = assertThrows(MissingYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsEmpty.yaml"));
            assertEquals(e.getMessage(), "Parameter in_directory is mandatory, but missing.");
        }

        /**
         * in_directory is invalid, if the directory doesn't exist.
         */
        @Test
        void test_creating_assertion_when_in_directory_is_no_directory_should_result_in_exception(){
            IllegalYamlParameterException e = assertThrows(IllegalYamlParameterException.class, () ->
                    yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +"testCaseInDirectoryIsNoDirectory.yaml"));
            assertEquals(e.getMessage(), "Illegal value for assertion parameter in_directory.");
        }
    }

    /**
     * Tests checking the FileCountAssertion
     */
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class AssertionExecutionTest{

        /**
         * Creates an assertion by calling getTestCaseFromYaml with a valid yaml file. The assertion is expected to
         * be true.
         * @throws FileNotFoundException can be thrown when loading the yaml file, but isn't expected.
         */
        @Test
        void test_executing_fulfilled_assertion_should_result_in_true() throws IOException {
            Assertion assertion = yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +
                    "testCaseAssertionFulfilled.yaml").getAssertion();
            assertTrue(assertion instanceof FileCountAssertion);
            FileCountAssertion fileCountAssertion = (FileCountAssertion) assertion;
            assertTrue(fileCountAssertion.checkAssertion());
        }

        /**
         * Creates an assertion by calling getTestCaseFromYaml with a valid yaml file. The assertion is expected to
         * be false.
         * @throws FileNotFoundException can be thrown when loading the yaml file, but isn't expected.
         */
        @Test
        void test_executing_unfulfilled_assertion_should_result_in_false() throws IOException {
            Assertion assertion = yamlParser.getTestCaseFromYaml(PATH_TEST_FILES +
                    "testCaseAssertionUnfulfilled.yaml").getAssertion();
            assertTrue(assertion instanceof FileCountAssertion);
            FileCountAssertion fileCountAssertion = (FileCountAssertion) assertion;
            assertFalse(fileCountAssertion.checkAssertion());
        }

    }
}
