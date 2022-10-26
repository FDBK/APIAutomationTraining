package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class APICoreRequests {

    @Step("Make a GET-Request")
    public Response makeGetRequest(String url) {

        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();

    }

    @Step("Make a GET-Request with token and cookie")
    public Response makeGetRequestWithTokenAndCookie(String url, String token, String cookie) {

        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();

    }

    @Step("Make a GET-Request with cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {

        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();

    }

    @Step("Make a GET-Request with token only")
    public Response makeGetRequestWithToken(String url, String token) {

        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();

    }

    @Step("Make a POST-Request")
    public Response makePostRequest(String url, Map<String, String> authData) {

        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();

    }

    @Step("Make a PUT-Request with token, cookie and data")
    public Response makePutRequestToEditData(String url, String token, String cookie, Map<String, String> editData) {

        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();

    }

    @Step("Make a PUT-Request without authorization")
    public Response makePutRequestToEditDataWithoutAuthorization(String url, Map<String, String> editData) {

        return given()
                .filter(new AllureRestAssured())
                .body(editData)
                .put(url)
                .andReturn();

    }

    @Step("Make a GET-Request (JsonPath")
    public JsonPath makeGetRequestJSON(String url) {

        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .jsonPath();

    }

    @Step("Make a POST-Request (JsonPath)")
    public JsonPath makePostRequestJSON(String url, Map<String, String> authData) {

        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .jsonPath();

    }

}
