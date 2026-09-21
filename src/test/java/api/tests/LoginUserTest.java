package api.tests;

import api.client.StellarBurgersClientApi;
import api.model.User;
import api.utils.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static api.utils.StatusCodes.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Тесты для эндпоинта POST /api/auth/login
 * Проверяют вход под существующим пользователем и вход с неверным логином и паролем
 */
public class LoginUserTest {
    private StellarBurgersClientApi client;
    private User user;

    @Before
    public void setUp() {
        client = new StellarBurgersClientApi();
        user = UserGenerator.randomUser();

        client.createUser(user)
                .then()
                .statusCode(OK);
    }

    /**
     * Проверяет успешный вход с корректными почтой и паролем.
     * Отправляем POST /api/auth/login с валидными данными.
     * Ожидаем: 200 OK, success=true, наличие accessToken и refreshToken.
     */
    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Успешный вход с корректными почтой и паролем")
    public void loginExistingUser() {
        client.loginUser(user.getEmail(), user.getPassword())
                .then()
                .statusCode(OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    /**
     * Проверяет вход с неправильным паролем
     * Отправляем корректную почту, но неверный пароль
     * Ожидаем: 401 Unauthorized, success=false, сообщение об ошибке
     */
    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Вход с неправильным паролем должен вернуть ошибку 401")
    public void loginWithWrongPassword() {
        client.loginUser(user.getEmail(), "wrongpass")
                .then()
                .statusCode(UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
    /**
     * Проверяет вход с неправильной почтой
     * Отправляем корректный пароль, но неверную почту
     * Ожидаем: 401 Unauthorized, success=false, сообщение об ошибке
     */
    @Test
    @DisplayName("Логин с неверной почтой")
    @Description("Вход с неправильной почтой должен вернуть ошибку 401")
    public void loginWithWrongEmail() {
        client.loginUser("wrong@puh.ru", user.getPassword())
                .then()
                .statusCode(UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
