package api.steps;

import api.client.StellarBurgersApiClient;
import api.model.Order;
import io.qameta.allure.Step;

import static api.utils.StatusCodes.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Шаги для работы с заказами
 * добавлен как отдельный класс для чистоты кода после ревью
 * "Для всех методов, описывающих шаги теста, необходимо использовать аннотацию @step"
 */
public class OrderSteps {
    private final StellarBurgersApiClient client = new StellarBurgersApiClient();

    @Step("Создать заказ с авторизацией и проверить успех")
    public void createOrderAndCheckSuccess(String accessToken, Order order) {
        client.createOrder(accessToken, order)
                .then()
                .statusCode(OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    /**
     * ВНИМАНИЕ: по документации API ожидается 401 Unauthorized,
     * но по факту сервер принимает заказ без токена и возвращает 200 OK
     * Тест зафиксировал поведение API, а не документацию
     */
    @Step("Проверить ошибку: заказ без авторизации")
    public void createWithoutAuthAndCheckError(Order order) {
        client.createOrderWithoutAuth(order)
                .then()
                .statusCode(UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверить ошибку: заказ без ингредиентов")
    public void createWithoutIngredientsAndCheckError(String accessToken, Order order) {
        client.createOrder(accessToken, order)
                .then()
                .statusCode(BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверить ошибку: заказ с невалидным хешем ингредиента")
    public void createWithInvalidIngredientAndCheckError(String accessToken, Order order) {
        client.createOrder(accessToken, order)
                .then()
                .statusCode(INTERNAL_SERVER_ERROR);
    }
}
