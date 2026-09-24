package api.tests;

import api.model.User;
import api.steps.UserSteps;
import api.utils.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;


/**
 * Тесты для эндпоинта POST /api/auth/login
 * Проверяют вход под существующим пользователем и вход с неверным логином и паролем
 * после ревью изменения, тк добавила Steps-классы, объем кода стал меньше в тестовых классах
 */
public class LoginUserTest {
    private UserSteps userSteps;
    private User user;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = UserGenerator.randomUser();
        userSteps.createUser(user);
    }

    /**
     * Проверяет успешный вход с корректными почтой и паролем
     * Отправляем POST /api/auth/login с валидными данными
     * Ожидаем: 200 OK
     */
    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Успешный вход с корректными почтой и паролем")
    public void loginExistingUser() {
        userSteps.loginAndCheckSuccess(user.getEmail(), user.getPassword());
    }

    /**
     * Проверяет вход с неправильным паролем
     * Отправляем корректную почту, но неверный пароль
     * Ожидаем: 401 Unauthorized
     */
    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Вход с неправильным паролем должен вернуть ошибку 401")
    public void loginWithWrongPassword() {
        userSteps.loginAndCheckError(user.getEmail(), "wrongPassword");
    }
}
