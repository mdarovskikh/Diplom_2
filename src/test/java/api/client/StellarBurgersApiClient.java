package api.client;

import api.model.LoginRequest;
import api.model.Order;
import api.model.OrderRequest;
import api.model.RegisterRequest;
import api.model.User;
import com.google.gson.Gson;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Клиент API Stellar Burgers. Теперь здесь только HTTP
 * Тела запросов сериализуются через Gson
 */
public class StellarBurgersApiClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";
    private final Gson gson = new Gson();

    static {
        RestAssured.filters(new AllureRestAssured());
    }

    /** POST /api/auth/register — регистрация пользователя. */
    public Response createUser(User user) {
        RegisterRequest body = new RegisterRequest(
                user.getEmail(), user.getPassword(), user.getName());
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/register");
    }

    /** POST /api/auth/login — авторизация. */
    public Response loginUser(String email, String password) {
        LoginRequest body = new LoginRequest(email, password);
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(gson.toJson(body))
                .when()
                .post("/api/auth/login");
    }

    /** POST /api/orders — создание заказа с авторизацией. */
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

    /** POST /api/orders — создание заказа без авторизации. */
    public Response createOrderWithoutAuth(Order order) {
        OrderRequest body = new OrderRequest(order.getIngredients());
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(gson.toJson(body))
                .when()
                .post("/api/orders");
    }

    /** DELETE /api/auth/user — удаление пользователя по токену. */
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