package com.psi.app;

import com.psi.app.exceptions.IllegalYamlParameterException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit test for parsing of yaml
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ParseYamlTest {

    private static final String PATH_TEST_FILES = "src\\test\\resources\\yaml_test_files\\parse_yaml\\";
    private static final YamlParser yamlParser = new YamlParser();

    /**
     * Tests loading a valid yaml file, which is expected to result in a test case including a trigger and a assertion.
     * @throws FileNotFoundException can be thrown when loading the yaml file, but isn't expected.
     */
    @Test
    void test_parsing_yaml_from_correct_file_should_result_in_valid_test_case() throws IOException {
        TestCase testCase = yamlParser.getTestCaseFromYaml(PATH_TEST_FILES + "testCaseParseYaml.yaml");
        assertNotNull(testCase);
        assertNotNull(testCase.getTrigger());
        assertNotNull(testCase.getAssertion());
    }

    /**
     * Tests loading the example yaml file, which has a different syntax than normally processable by snakeyaml. Input
     * is adapted, so it can be parsed, and is expected to result in a test case including a trigger and an assertion.
     * @throws FileNotFoundException can be thrown when loading the yaml file, but isn't expected.
     */
    @Test
    void test_parsing_yaml_from_example_yaml_file_should_result_in_valid_test_case() throws IOException {
        TestCase testCase = yamlParser.getTestCaseFromYaml(PATH_TEST_FILES + "testCaseExampleSyntax.yaml");
        assertNotNull(testCase);
        assertNotNull(testCase.getTrigger());
        assertNotNull(testCase.getAssertion());
    }

    /**
     * Tries to load a yaml file from an invalid path, is expected to throw a FileNotFoundException.
     */
    @Test
    void test_parsing_yaml_from_invalid_path_should_result_in_exception() {
        assertThrows(FileNotFoundException.class, () ->
                yamlParser.getTestCaseFromYaml(PATH_TEST_FILES + "testCaseIllegal.yaml"));
    }

    /**
     * Tries to load a yaml file with invalid syntax, is expected to throw an org.yaml.snakeyaml.scanner.ScannerException
     * when parsing the file.
     */
    @Test
    void test_parsing_yaml_when_syntax_is_illegal_should_result_in_exception() {
        org.yaml.snakeyaml.parser.ParserException e = assertThrows(org.yaml.snakeyaml.parser.ParserException.class, () ->
                yamlParser.getTestCaseFromYaml(PATH_TEST_FILES + "testCaseIllegalSyntax.yaml"));
    }

    /**
     * Tries to load a yaml file, where the When part is not a Map, the file therefore doesn't match the required
     * structure. Is expected to throw an IllegalYamlParameterException.
     */
    @Test
    void test_parsing_yaml_when_When_is_no_map_should_result_in_exception() {
        assertThrows(IllegalYamlParameterException.class, () ->
                yamlParser.getTestCaseFromYaml(PATH_TEST_FILES + "testCaseWhenIsNoMap.yaml"));
    }

    /**
     * Tries to load a yaml file, where the Then part is not a Map, the file therefore doesn't match the required
     * structure. Is expected to throw an IllegalYamlParameterException.
     */
    @Test
    void test_parsing_yaml_when_Then_is_no_map_should_result_in_exception() {
        assertThrows(IllegalYamlParameterException.class, () ->
                yamlParser.getTestCaseFromYaml(PATH_TEST_FILES + "testCaseThenIsNoMap.yaml"));
    }

}