package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.APICoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User Data Retrieval Cases")
@Feature("User Data Retrieval")
public class UserGetTests extends BaseTestCase {

    private final APICoreRequests apiCoreRequests = new APICoreRequests();

    @Test
    @Description("This test attempts to correctly get user's data")
    @DisplayName("Positive User Data Retrieval Test")
    @Story("Positive User Data Retrieval Tests")
    @Severity(value = SeverityLevel.BLOCKER)
    public void testGetUserDetailsAuthAsSameUser() {

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

//        Response responseGetAuth = RestAssured
//                .given()
//                .body(authData)
//                .post("https://playground.learnqa.ru/api/user/login")
//                .andReturn();

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/2",
                header,
                cookie);

//        Response responseUserData = RestAssured
//                .given()
//                .header("x-csrf-token", header)
//                .cookie("auth_sid", cookie)
//                .get("https://playground.learnqa.ru/api/user/2")
//                .andReturn();

        String[] expectedFields = {"username", "firstName", "lastName", "email"};

        Assertions.assertJsonHasFields(responseUserData, expectedFields);

    }

    @Test
    @Description("This test attempts to get user's data without authorization")
    @DisplayName("Negative User Data Retrieval Test - No authorization")
    @Story("Negative User Data Retrieval Tests")
    public void testGetUserDataNotAuth() {

        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/2");

//        Response responseUserData = RestAssured
//                .get("https://playground.learnqa.ru/api/user/2")
//                .andReturn();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");

    }

    @Test
    @Description("This test attempts to get another user's data")
    @DisplayName("Negative User Data Retrieval Test - Another user")
    @Story("Negative User Data Retrieval Tests")
    public void testGetUserDetailsAuthAsAnotherUser() {

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/1",
                header,
                cookie);

        String[] unexpectedFields = {"firstName", "lastName", "email"};

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotFields(responseUserData, unexpectedFields);

    }

}