import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HelloWorldTest {

    @Test
    public void testHelloWorld() {
        Map<String,Object> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");
        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();

        response.print();


        //Map<String, String> params = new HashMap<>();
        //params.put("name","John");

        //JsonPath response = RestAssured
        //        .given()
        //        .queryParams(params)
        //        .get("https://playground.learnqa.ru/api/hello")
        //        .jsonPath();
        //String answer = response.get("answer");
        //if (answer == null) {
        //    System.out.println("The key 'answer' is absent!");
        //} else {
        //    System.out.println(answer);
        //}


        //Response response = RestAssured
        //        .get("https://playground.learnqa.ru/api/get_text")
        //        .andReturn();
        //response.prettyPrint();


        //System.out.println("Hello from FDBK");
    }

}
