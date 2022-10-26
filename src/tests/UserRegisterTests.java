package tests;

import io.restassured.response.Response;
import lib.APICoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class UserRegisterTests extends BaseTestCase {

    private final APICoreRequests apiCoreRequests = new APICoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {

        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user",
                userData);

//        Response responseCreateAuth = RestAssured
//                .given()
//                .body(userData)
//                .post("https://playground.learnqa.ru/api/user")
//                .andReturn();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }

    @Test
    public void testCreateUserSuccessful() {

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user",
                userData);

//        Response responseCreateAuth = RestAssured
//                .given()
//                .body(userData)
//                .post("https://playground.learnqa.ru/api/user")
//                .andReturn();

        System.out.println(userData);
        responseCreateAuth.prettyPrint();

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");

    }

    @Test
    public void testCreateUserWithIncorrectEmail() {

        String email = "vinkotov.example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");

    }

    public static Stream registrationDataProvider() {

        String email = DataGenerator.getRandomEmail();

        return Stream.of(
                Arguments.of("", "123", "learnqa", "learnqa", "learnqa"),
                Arguments.of(email, "", "learnqa", "learnqa", "learnqa"),
                Arguments.of(email, "123", "", "learnqa", "learnqa"),
                Arguments.of(email, "123", "learnqa", "", "learnqa"),
                Arguments.of(email, "123", "learnqa", "learnqa", ""));

    }

    @ParameterizedTest
    @MethodSource("registrationDataProvider")
    public void testCreateUserWithoutSomeParameter(String email, String password, String username, String firstName, String lastName) {

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("username", username);
        userData.put("firstName", firstName);
        userData.put("lastName", lastName);

        String missingParameter = "";
        if (email.equals("")) {
            userData.remove("email");
            missingParameter = "email";
        } else if (password.equals("")) {
            userData.remove("password");
            missingParameter = "password";
        } else if (username.equals("")) {
            userData.remove("username");
            missingParameter = "username";
        } else if (firstName.equals("")) {
            userData.remove("firstName");
            missingParameter = "firstName";
        } else if (lastName.equals("")) {
            userData.remove("lastName");
            missingParameter = "lastName";
        }

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + missingParameter);

    }

    @Test
    public void testCreateUserWithVeryShortUsername() {

        Map<String, String> userData = new HashMap<>();
        userData.put("username", "1");

        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");

    }

    @Test
    public void testCreateUserWithVeryLongUsername() {

        Map<String, String> userData = new HashMap<>();
        userData.put("username", RandomStringUtils.randomAlphanumeric(251));

        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user",
                userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");

    }

}