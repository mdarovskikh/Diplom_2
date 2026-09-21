import client.StellarBurgersClientApi;
import api.model.User;
import api.utils.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Тесты для эндпоинта POST /api/auth/register.
 * Проверяют три сценария:
 * - создание уникального пользователя
 * - создание пользователя, который уже зарегистрирован
 * - создание пользователя без обязательного поля (нп без email).
 */
public class CreateUserTest {
    private StellarBurgersClientApi client;
    private User user;

    @Before
    public void setUp() {
        client = new StellarBurgersClientApi();
        user = UserGenerator.randomUser();
    }

    /**
     * Проверяет успешное создание нового пользователя
     * Отправляем POST /api/auth/register с валидными данными
     * Ожидаем: 200 OK, success=true и наличие accessToken/refreshToken в ответе
     */
    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяем, что нового пользователя можно создать с валидными данными")
    public void createUniqueUser() {
        client.createUser(user)
                .then()
                .statusCode(OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    /**
     * Проверяет попытку зарегистрировать уже существующего пользователя
     * Ожидаем: 403 Forbidden, success=false, сообщение "User already exists"
     */
    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Повторная регистрация с теми же данными должна вернуть ошибку 403")
    public void createExistingUser() {
        client.createUser(user);
        client.createUser(user)
                .then()
                .statusCode(FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    /**
     * Проверяет регистрацию без обязательного поля
     * Отправляем null вместо email, остальные поля заполнены
     * Ожидаем: 403 Forbidden
     */
    @Test
    @DisplayName("Создание пользователя без обязательного поля Email")
    @Description("Если не заполнить email, должна вернуться ошибка 403")
    public void createUserWithoutEmail() {
        User userWithoutEmail = new User(null, "p123", "puh");

        client.createUser(userWithoutEmail)
                .then()
                .statusCode(FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));

    }
}
