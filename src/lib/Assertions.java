package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Assertions {

    @Step("Assert that '{name}' value is '{expectedValue}'")
    public static void assertJsonByName(Response response, String name, int expectedValue) {

        response.then().assertThat().body("$", hasKey(name));

        int value = response.jsonPath().getInt(name);

        assertEquals(expectedValue, value, "Error! JSON value is not equal to the expected value.");

    }

    @Step("Assert that '{name}' value is '{expectedValue}'")
    public static void assertJsonByName(Response response, String name, String expectedValue) {

        response.then().assertThat().body("$", hasKey(name));

        String value = response.jsonPath().getString(name);

        assertEquals(expectedValue, value, "Error! JSON value is not equal to the expected value.");

    }

    @Step("Assert that there is '{expectedFieldName}' field in response")
    public static void assertJsonHasField(Response response, String expectedFieldName) {

        response.then().assertThat().body("$", hasKey(expectedFieldName));

    }

    @Step("Assert that there is no '{unexpectedFieldName}' field in response")
    public static void assertJsonHasNotField(Response response, String unexpectedFieldName) {

        response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));

    }

    @Step("Assert that there are '{expectedFieldNames}' fields in response")
    public static void assertJsonHasFields(Response response, String[] expectedFieldNames) {

        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasField(response, expectedFieldName);
        }

    }

    @Step("Assert that there are no '{unexpectedFieldNames}' fields in response")
    public static void assertJsonHasNotFields(Response response, String[] unexpectedFieldNames) {

        for (String unexpectedFieldName : unexpectedFieldNames) {
            Assertions.assertJsonHasNotField(response, unexpectedFieldName);
        }

    }

    @Step("Assert that response's text is '{expectedAnswer}'")
    public static void assertResponseTextEquals (Response response, String expectedAnswer) {

        assertEquals(
                expectedAnswer,
                response.asString(),
                "Error! Unexpected response text."
        );

    }

    @Step("Assert that response's status code is '{expectedStatusCode}'")
    public static void assertResponseCodeEquals (Response response, int expectedStatusCode) {

        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "Error! Unexpected response status code."
        );

    }

    @Step("Assert that response's text is not '{unexpectedAnswer}'")
    public static void assertResponseTextNotEquals (Response response, String unexpectedAnswer) {

        assertNotEquals(
                unexpectedAnswer,
                response.asString(),
                "Error! Unexpected response text."
        );

    }

    @Step("Assert that response's status code is not '{unexpectedStatusCode}'")
    public static void assertResponseCodeNotEquals (Response response, int unexpectedStatusCode) {

        assertNotEquals(
                unexpectedStatusCode,
                response.statusCode(),
                "Error! Unexpected response status code."
        );

    }

}