package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.APICoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserEditTests extends BaseTestCase {

    private final APICoreRequests apiCoreRequests = new APICoreRequests();

    @Test
    public void testEditJustCreatedUser() {

        // GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = apiCoreRequests.makePostRequestJSON(
                "https://playground.learnqa.ru/api/user/",
                userData);

//        JsonPath responseCreateAuth = RestAssured
//                .given()
//                .body(userData)
//                .post("https://playground.learnqa.ru/api/user/")
//                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        // LOG IN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

//        Response responseGetAuth = RestAssured
//                .given()
//                .body(authData)
//                .post("https://playground.learnqa.ru/api/user/login")
//                .andReturn();

        // EDIT USER DATA
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestToEditData(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

//        Response responseEditUser = RestAssured
//                .given()
//                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
//                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
//                .body(editData)
//                .put("https://playground.learnqa.ru/api/user/" + userId)
//                .andReturn();

        // GET USER DATA
        Response responseUserData = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"));

//        Response responseUserData = RestAssured
//                .given()
//                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
//                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
//                .get("https://playground.learnqa.ru/api/user/" + userId)
//                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }

    @Test
    public void testUnauthorizedEdit() {

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "New Name");
        editData.put("lastName", "New Name");

        Response responseEditUser = apiCoreRequests.makePutRequestToEditDataWithoutAuthorization(
                "https://playground.learnqa.ru/api/user/",
                editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");

    }

    @Test
    public void testEditUserAuthAsAnotherUser() {

        // LOG IN AS FIRST USER (ID 48730)
        Map<String, String> authData1 = new HashMap<>();
        authData1.put("email", "learnqa20221026221518@example.com");
        authData1.put("password", "123");

        Response responseGetAuth1 = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData1);

        String userId1 = responseGetAuth1.jsonPath().getString("user_id");
        assertEquals("48730", userId1, "Error! This user's ID is not 48730, it's " + userId1 +".");

        // EDIT SECOND USER's DATA (ID 48734)
        String newName = "New Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("username", newName);
        editData.put("firstName", newName);
        editData.put("lastName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequestToEditData(
                "https://playground.learnqa.ru/api/user/48734",
                this.getHeader(responseGetAuth1, "x-csrf-token"),
                this.getCookie(responseGetAuth1, "auth_sid"),
                editData);

        // LOG IN AS SECOND USER (ID 48734)
        Map<String, String> authData2 = new HashMap<>();
        authData2.put("email", "learnqa20221026224834@example.com");
        authData2.put("password", "123");

        Response responseGetAuth2 = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData2);

        String userId2 = responseGetAuth2.jsonPath().getString("user_id");
        assertEquals("48734", userId2, "Error! This user's ID is not 48734, it's " + userId2 +".");

        // CHECK SECOND USER's FULL DATA
        Response responseUserData = apiCoreRequests.makeGetRequestWithTokenAndCookie(
                "https://playground.learnqa.ru/api/user/48734",
                this.getHeader(responseGetAuth2, "x-csrf-token"),
                this.getCookie(responseGetAuth2, "auth_sid"));

        String username = responseUserData.jsonPath().getString("username");
        String firstName = responseUserData.jsonPath().getString("firstName");
        String lastName = responseUserData.jsonPath().getString("lastName");

        assertNotEquals(username, newName, "Error! Another user's Username was successfully changed.");
        assertNotEquals(firstName, newName, "Error! Another user's First Name was successfully changed.");
        assertNotEquals(lastName, newName, "Error! Another user's Last Name was successfully changed.");

    }

    @Test
    public void testEditEmailIncorrectly() {

        // LOG IN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "learnqa20221026221518@example.com");
        authData.put("password", "123");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String userId = responseGetAuth.jsonPath().getString("user_id");

        // EDIT DATA
        String newEmail = "learnqa.example.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests.makePutRequestToEditData(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        // CHECK RESULTS
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");

    }

    @Test
    public void testEditFirstNameVeryShort() {

        // LOG IN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "learnqa20221026221518@example.com");
        authData.put("password", "123");

        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData);

        String userId = responseGetAuth.jsonPath().getString("user_id");

        // EDIT DATA
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "1");

        Response responseEditUser = apiCoreRequests.makePutRequestToEditData(
                "https://playground.learnqa.ru/api/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData);

        // CHECK RESULTS
        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser, "error", "Too short value for field firstName");

    }

}