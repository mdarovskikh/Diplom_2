import client.StellarBurgersClientApi;
import api.model.Order;
import api.model.User;
import api.utils.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Тесты для эндпоинта POST /api/orders
 * Покрывают четыре сценария:
 * с авторизацией,
 * без авторизации,
 * без ингредиентов,
 * с невалидным хешем ингредиента
 * Тестовые данные ингредиентов вынесены в IngredientTestData для удобства
 */
public class CreateOrderTest {
    private StellarBurgersClientApi client;
    private String accessToken;

    @Before
    public void setUp() {
        client = new StellarBurgersClientApi();
        User user = UserGenerator.randomUser();
        client.createUser(user);

        accessToken = client.loginUser(user.getEmail(), user.getPassword())
                .then()
                .extract()
                .path(accessToken);
    }

    /**
     * Успешное создание заказа с авторизацией и полным набором ингредиентов
     * Отправляем POST /api/orders с валидным токеном и полным бургером
     * Ожидаем: 200 OK, success=true, номер заказа
     */
    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Успешное создание заказа с валидным токеном и ингредиентами")
    public void createOrderWithAuthAndIngredients() {
        Order order = new Order(FULL_BURGER_INGREDIENTS);

        client.createOrder(accessToken, order)
                .then()
                .statusCode(OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    /**
     * Создание заказа без авторизации
     * Отправляем POST /api/orders без заголовка Authorization
     * Ожидаем: 401 Unauthorized, сообщение "You should be authorised"
     */
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Запрос без токена должен вернуть 401")
    public void createOrderWithoutAuth() {
        Order order = new Order(BUN_ONLY_INGREDIENTS);

        client.createOrderWithoutAuth(order)
                .then()
                .statusCode(UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    /**
     * Создание заказа без ингредиентов
     * Отправляем POST /api/orders с авторизацией, но ingredients = []
     * Ожидаем: 400 Bad Request, сообщение "Ingredient ids must be provided".
     */
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("На запрос без ингредиентов должна вернуться ошибка 400")
    public void createOrderWithoutIngredients() {
        Order order = new Order(EMPTY_INGREDIENTS);

        client.createOrder(accessToken, order)
                .then()
                .statusCode(BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    /**
     * Создание заказа с неверным хешем ингредиентов
     * Отправляем POST /api/orders с авторизацией и ID, которого нет в базе
     * Ожидаем: 500 Internal Server Error
     */
    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Невалидный ID ингредиента должен вернуть 500")
    public void createOrderWithInvalidIngredientHash() {
        Order order = new Order(Collections.singletonList(INVALID_INGREDIENT_ID));

        client.createOrder(accessToken, order)
                .then()
                .statusCode(INTERNAL_SERVER_ERROR);
    }
}
