package api.tests;

import api.client.StellarBurgersApiClient;
import api.model.Order;
import api.model.User;
import api.steps.OrderSteps;
import api.steps.UserSteps;
import api.utils.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import static api.data.IngredientData.*;
import static api.utils.StatusCodes.*;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Тесты для эндпоинта POST /api/orders
 * Покрывают четыре сценария:
 * с авторизацией,
 * без авторизации,
 * без ингредиентов,
 * с невалидным хешем ингредиента
 * Тестовые данные ингредиентов вынесены в IngredientTestData для удобства
 * после ревью изменения, тк добавила Steps-классы, объем кода стал меньше в тестовых классах
 */
public class CreateOrderTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private String accessToken;

    @Before
    public void setUp() {
       userSteps = new UserSteps();
       orderSteps = new OrderSteps();

       User user = UserGenerator.randomUser();
       userSteps.createUser(user);
       accessToken = userSteps.getAccessToken(user.getEmail(), user.getPassword());
    }

    /**
     * Успешное создание заказа с авторизацией и полным набором ингредиентов
     * Отправляем POST /api/orders с валидным токеном и полным бургером
     * Ожидаем: 200 OK и номер заказа
     */
    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Успешное создание заказа с валидным токеном и ингредиентами")
    public void createOrderWithAuthAndIngredients() {
        orderSteps.createOrderAndCheckSuccess(accessToken, new Order(FULL_BURGER_INGREDIENTS));
    }

    /**
     * Создание заказа без авторизации
     * <p>
     * ВНИМАНИЕ: по документации API ожидается 401 Unauthorized,
     * но по факту сервер принимает заказ без токена и возвращает 200 OK
     * Тест зафиксировал реальное поведение API
     */
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("API фактически принимает заказ без токена и возвращает 200 OK")
    public void createOrderWithoutAuth() {
        orderSteps.createWithoutAuthAndCheckError(new Order(BUN_ONLY_INGREDIENTS));
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
        orderSteps.createWithoutIngredientsAndCheckError(accessToken, new Order(EMPTY_INGREDIENTS));
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
        orderSteps.createWithInvalidIngredientAndCheckError(accessToken, order);
    }
}
