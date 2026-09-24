package api.client;

import api.model.*;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Убрана ручная сборка JSON через String.format.
 */
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
    @Step("POST /api/auth/register — создание пользователя {user.email}")
    public Response createUser(User user) {
        RegisterRequest body = new RegisterRequest(
                user.getEmail(),
                user.getPassword(),
                user.getName()
        );
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/register");
    }

    /**
     * POST /api/auth/login — авторизация пользователя
     * ответ сервера содержит accessToken и refreshToken при успешном запросе
     */
    @Step("POST /api/auth/login — авторизация {email}")
    public Response loginUser(String email, String password) {
        LoginRequest body = new LoginRequest(email, password);
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/login");
    }

    // == ЗАКАЗЫ ==
    /**
     * POST /api/orders — создание заказа с авторизацией
     */
    @Step("POST /api/orders — создание заказа с авторизацией")
    public Response createOrder(String accessToken, Order order) {
        OrderRequest body = new OrderRequest(order.getIngredients());

        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .header("Authorization", accessToken)
                .body(gson.toJson(body))
                .when()
                .post("/api/orders");
    }

    /**
     * POST /api/orders — создание заказа без авторизации
     * Используется для проверки ошибки 401 Unauthorized
     */
    @Step("POST /api/orders — создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        OrderRequest body = new OrderRequest(order.getIngredients());

        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(gson.toJson(body))
                .when()
                .post("/api/orders");
    }

    // === ИНГРЕДИЕНТЫ ===
    /**
     * GET /api/ingredients — получение списка всех доступных ингредиентов
     */
    @Step("GET /api/ingredients — получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .when()
                .get("/api/ingredients");
    }
}
