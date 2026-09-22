package api.client;

import api.model.Order;
import api.model.User;
import com.google.gson.Gson;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StellarBurgersClientApi {
    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private final Gson gson = new Gson();
    static {
        RestAssured.filters(new AllureRestAssured());
    }

    // = ПОЛЬЗОВАТЕЛИ =
    /**
     * POST /api/auth/register — создание пользователя
     */
    public Response createUser(User user) {
        return given()
                .baseUri(BASE_URL)
                .contentType(io.restassured.http.ContentType.JSON)
                .body(gson.toJson(user))
                .when()
                .post("/api/auth/register");
    }

    /**
     * POST /api/auth/login — авторизация пользователя
     * ответ сервера содержит accessToken и refreshToken при успешном запросе
     */
    public Response loginUser(String email, String password) {
        String body = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
        return given()
                .baseUri(BASE_URL)
                .contentType(io.restassured.http.ContentType.JSON)
                .body(body)
                .log().all()
                .when()
                .post("/api/auth/login")
                .then()
                .log().all()
                .extract()
                .response();
    }

    // == ЗАКАЗЫ ==
    /**
     * POST /api/orders — создание заказа с авторизацией
     */
    public Response createOrder(String accessToken, Order order) {
        return given()
                .baseUri(BASE_URL)
                .contentType(io.restassured.http.ContentType.JSON)
                .header("Authorization", accessToken)
                .body(gson.toJson(order))
                .when()
                .post("/api/orders");
    }

    /**
     * POST /api/orders — создание заказа без авторизации
     * Используется для проверки ошибки 401 Unauthorized
     */
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .baseUri(BASE_URL)
                .contentType(io.restassured.http.ContentType.JSON)
                .body(gson.toJson(order))
                .log().all()
                .when()
                .post("/api/orders")
                .then()
                .log().all()
                .extract()
                .response();
    }

    // === ИНГРЕДИЕНТЫ ===
    /**
     * GET /api/ingredients — получение списка всех доступных ингредиентов
     */
    public Response getIngredients() {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .when()
                .get("/api/ingredients");
    }
}
