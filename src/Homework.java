import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class Homework {

    @Test
    public void HomeworkTest() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String message_no_2 = response.get("messages[1].message");
        System.out.println(message_no_2);

    }

}