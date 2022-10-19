package Homeworks;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework_11 {

    @Test
    public void testAssertHomeworkCookie() {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        String homeworkCookie = response.getCookie("HomeWork");
        assertEquals("hw_value", homeworkCookie, "Error! Incorrect cookie value: " + homeworkCookie + ".");

    }

}