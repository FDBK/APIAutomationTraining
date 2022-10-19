package Homeworks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework_11 {

    @ParameterizedTest
    @ValueSource(strings = {"Short string", "String with more than 15 symbols"})
    public void testCheckLength(String string) {

        assertTrue(string.length() > 15, "Error! The string's length is less than 15, it's " + string.length());

    }

}