import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class Homework_8 {

    @Test
    public void HomeworkTest() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = response.get("token");
        int seconds = response.get("seconds");
        System.out.println("Token for your job is " + token + ". This job will be done in " + seconds + " seconds.");

        response = RestAssured
                .given()
                .queryParam("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String status = response.get("status");
        if (status.equals("Job is ready")) {
            System.out.println("Something went wrong... the job is already done!");
        } else {
            sleepFor(seconds * 1000);

            response = RestAssured
                    .given()
                    .queryParam("token", token)
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();

            status = response.get("status");
            String result = response.get("result");
            if (status.equals("Job is ready") && !(result == null)) {
                System.out.println("Everything went fine! Here's the result: " + result);
            } else {
                System.out.println("Something went wrong!");
            }
        }
    }

    public void sleepFor(long time_to_sleep)
    {
        try {
            Thread.sleep(time_to_sleep);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

}