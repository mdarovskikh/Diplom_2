package api;

import api.model.RegisterRequest;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Клиент для работы с API пользователей - создание и удаление
 */
public class UserApiClient {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private final Gson gson = new Gson();

    @Step("Создание пользователя через API: {email}")
    public String createUser(String email, String password, String name) {
        RegisterRequest body = new RegisterRequest(email, password, name);

        Response response = given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/register");
        return response.statusCode() == 200
                ? response.then().extract().path("accessToken")
                : null;
    }

    @Step("Удаление пользователя через API по токену")
    public void deleteUser(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }
}
