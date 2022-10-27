package lib;

import io.qameta.allure.Step;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.*;

public class BaseTestCase {

    @Step("Get header from response: '{name}'")
    protected String getHeader (Response response, String name) {

        Headers headers = response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Error! Response does not contain the '" + name + "' header.");

        return headers.getValue(name);

    }

    @Step("Get cookie from response: '{name}'")
    protected String getCookie (Response response, String name) {

        Map<String, String> cookies = response.getCookies();

        assertTrue(cookies.containsKey(name), "Error! Response does not contain the '" + name + "' cookie.");

        return cookies.get(name);

    }

    @Step("Get integer from response: '{name}'")
    protected int getIntFromJson (Response response, String name) {

        response.then().assertThat().body("$", hasKey(name));

        return response.jsonPath().getInt(name);

    }

}