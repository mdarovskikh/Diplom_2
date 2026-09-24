package api.steps;

import api.client.StellarBurgersApiClient;
import api.model.User;
import io.qameta.allure.Step;

import static api.utils.StatusCodes.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Шаги для работы с пользователями
 * добавлен как отдельный класс для чистоты кода после ревью
 * "Для всех методов, описывающих шаги теста, необходимо использовать аннотацию @step"
 */
public class UserSteps {
    private final StellarBurgersApiClient client = new StellarBurgersApiClient();

    @Step("Создать пользователя: {user.email}")
    public void createUser(User user) {
        client.createUser(user);
    }

    @Step("Создать пользователя {user.email} и проверить успешность создания")
    public void createUserAndCheckSuccess(User user) {
        client.createUser(user)
                .then()
                .statusCode(OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверить ошибку повторной регистрации: {user.email}")
    public void createDuplicateAndCheckError(User user) {
        client.createUser(user)
                .then()
                .statusCode(FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверить ошибку при отсутствии обязательных полей")
    public void createWithoutRequiredFieldsAndCheckError(User user) {
        client.createUser(user)
                .then()
                .statusCode(FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Авторизоваться {email} и проверить успешность входа")
    public void loginAndCheckSuccess(String email, String password) {
        client.loginUser(email, password)
                .then()
                .statusCode(OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверить ошибку авторизации: {email} / {wrongPassword}")
    public void loginAndCheckError(String email, String wrongPassword) {
        client.loginUser(email, wrongPassword)
                .then()
                .statusCode(UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Получить accessToken для {email}")
    public String getAccessToken(String email, String password) {
        return client.loginUser(email, password)
                .then()
                .extract()
                .path("accessToken");
    }

    @Step("Удалить пользователя по токену")
    public void deleteUser(String accessToken) {
        client.deleteUser(accessToken);
    }
}
