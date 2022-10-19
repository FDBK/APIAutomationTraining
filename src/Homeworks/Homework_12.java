package Homeworks;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Homework_12 {

    @Test
    public void testAssertHomeworkHeader() {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        String homeworkHeader = response.getHeader("x-secret-homework-header");
        assertEquals("Some secret value", homeworkHeader, "Error! Incorrect header value: " + homeworkHeader + ".");

    }

}