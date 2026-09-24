package api.tests;

import api.model.User;
import api.steps.UserSteps;
import api.utils.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

/**
 * Тесты для эндпоинта POST /api/auth/register
 * Проверяют три сценария:
 * - создание уникального пользователя
 * - создание пользователя, который уже зарегистрирован
 * - создание пользователя без обязательного поля (нп без email)
 * после ревью изменения, тк добавила Steps-классы, объем кода стал меньше в тестовых классах
 */
public class CreateUserTest {
    private UserSteps userSteps;
    private User user;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = UserGenerator.randomUser();
    }

    /**
     * Проверяет успешное создание нового пользователя
     * Отправляем POST /api/auth/register с валидными данными
     * Ожидаем: 200 OK
     */
    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяем, что нового пользователя можно создать с валидными данными")
    public void createUniqueUser() {
        userSteps.createUserAndCheckSuccess(user);
    }

    /**
     * Проверяет попытку зарегистрировать уже существующего пользователя
     * Ожидаем: 403 Forbidden
     */
    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Повторная регистрация с теми же данными должна вернуть ошибку 403")
    public void createExistingUser() {
        userSteps.createUser(user);
        userSteps.createDuplicateAndCheckError(user);
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
        User withoutEmail = new User(null, "password", "Name");
        userSteps.createWithoutRequiredFieldsAndCheckError(withoutEmail);
    }
}
