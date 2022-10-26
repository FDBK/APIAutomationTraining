package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.APICoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

@Epic("Authorization Cases")
@Feature("Authorization")
public class UserAuthTests extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final APICoreRequests apiCoreRequests = new APICoreRequests();

    @BeforeEach
    public void loginUser(){

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest(
                        "https://playground.learnqa.ru/api/user/login",
                        authData);

        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

    }

    @Test
    @Description("This test successfully authorizes the user by username and password")
    @DisplayName("Positive User Authorization")
    public void testAuthUser() {

        Response responseCheckAuth = apiCoreRequests
                .makeGetRequestWithTokenAndCookie(
                        "https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie);

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);

    }

    @ParameterizedTest
    @Description("This test checks authorization without sending headers or cookies")
    @DisplayName("Negative User Authorization")
    @ValueSource(strings = {"cookie", "headers"})
    public void testAuthUserNegative(String condition) {

        RequestSpecification spec = RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else if (condition.equals("headers")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header);
            Assertions.assertJsonByName(responseForCheck, "user_id", 0);
        } else {
            throw new IllegalArgumentException("Error! Condition value '" + condition + "' is unknown.");
        }

    }

}