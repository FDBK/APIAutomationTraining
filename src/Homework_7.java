import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Homework_7 {

    @Test
    public void HomeworkTest() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        int statusCode = response.getStatusCode();

        while (statusCode == 301) {
            String redirectLocation = response.getHeader("Location");
            System.out.println("Redirecting to: " + redirectLocation);

            response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(redirectLocation)
                    .andReturn();

            statusCode = response.getStatusCode();
        }

        System.out.println("You've reached the final destination!");

    }

}