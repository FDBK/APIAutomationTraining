package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.APICoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("User Deletion Cases")
@Feature("User Deletion")
public class UserDeleteTests extends BaseTestCase {

    private final APICoreRequests apiCoreRequests = new APICoreRequests();

    @Test
    @Description("This test registers new user and successfully deletes this user")
    @DisplayName("Positive User Deletion Test")
    @Story("Positive User Deletion Tests")
    @Severity(value = SeverityLevel.BLOCKER)
    public void testDeleteJustCreatedUser() {

        // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJSON(
                "https://playground.learnqa.ru/api/user/",
                userData);

        String userId = responseCreateAuth.getString("id");

        // LOG IN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        // CHECK USER DATA BEFORE DELETION
        Response responseUserDataBeforeDeletion = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeNotEquals(responseUserDataBeforeDeletion, 404);
        Assertions.assertResponseTextNotEquals(responseUserDataBeforeDeletion, "User not found");

        // DELETE USER
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        // CHECK USER DATA AFTER DELETION
        Response responseUserDataAfterDeletion = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeEquals(responseUserDataAfterDeletion, 404);
        Assertions.assertResponseTextEquals(responseUserDataAfterDeletion, "User not found");

    }

    @Test
    @Description("This test attempts to delete the protected user")
    @DisplayName("Negative User Deletion Test - Protected user")
    @Story("Negative User Deletion Tests")
    public void testDeleteProtectedUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/2",
                header,
                cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }

    @Test
    @Description("This test attempts to delete another user")
    @DisplayName("Negative User Deletion Test - Another user")
    @Story("Negative User Deletion Tests")
    public void testDeleteUserAuthAsAnotherUser() {

        // LOG IN AS FIRST USER (ID 48730)
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "learnqa20221026221518@example.com");
        authData.put("password", "123");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        // CHECK SECOND USER BEFORE DELETION (ID 48734)
        Response responseUserDataBeforeDeletion = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/48734",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        String usernameBeforeDeletion = responseUserDataBeforeDeletion.jsonPath().getString("username");
        assertEquals(
                "learnqa",
                usernameBeforeDeletion,
                "Error! Unexpected username, '" + usernameBeforeDeletion + "' instead of 'learnqa'.");

        // DELETE SECOND USER (ID 48734)
        Response responseDeleteUser = apiCoreRequests.makeDeleteRequest(
                "https://playground.learnqa.ru/api/user/48734",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        // CHECK SECOND USER AFTER DELETION (ID 48734)
        Response responseUserDataAfterDeletion = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/48734",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

        Assertions.assertResponseCodeNotEquals(responseUserDataAfterDeletion, 404);
        Assertions.assertResponseTextNotEquals(responseUserDataAfterDeletion, "User not found");

        String usernameAfterDeletion = responseUserDataAfterDeletion.jsonPath().getString("username");
        assertEquals(
                usernameBeforeDeletion,
                usernameAfterDeletion,
                "Error! Unexpected username, '" + usernameAfterDeletion + "' instead of '" + usernameBeforeDeletion + "'.");

    }

}