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

}
