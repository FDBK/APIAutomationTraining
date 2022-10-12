import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Homework_6 {

    @Test
    public void HomeworkTest() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String redirectLocation = response.getHeader("Location");
        System.out.println(redirectLocation);

    }

}
