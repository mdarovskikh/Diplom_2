package api.data;

import java.util.List;
import java.util.Arrays;
/**
 * Тестовые данные ингредиентов
 * Валидные ID взяты из реального ответа GET /api/ingredients
 * Невалидные ID сконструированы специально для проверки ошибок сервера
 * Все данные сгруппированы по секциям: валидные ID, готовые наборы, невалидные ID
 */
public final class IngredientData {
    private IngredientData() {

    }
    // = ВАЛИДНЫЕ ID ИНГРЕДИЕНТОВ =
    /** Флюоресцентная булка R2-D3 (тип bun) */
    public static final String FLUORESCENT_BUN_ID = "61c0c5a71d1f82001bdaaa6d";
    /** Краторная булка N-200i (тип bun) */
    public static final String CRATER_BUN_ID = "61c0c5a71d1f82001bdaaa6c";
    /** Мясо бессмертных моллюсков Protostomia (тип main) */
    public static final String PROTOSTOMIA_MEAT_ID = "61c0c5a71d1f82001bdaaa6f";
    /** Соус Spicy-X (тип sauce) */
    public static final String SPICY_SAUCE_ID = "61c0c5a71d1f82001bdaaa72";
    /** Соус фирменный Space Sauce (тип sauce) */
    public static final String SPACE_SAUCE_ID = "61c0c5a71d1f82001bdaaa73";


    // = ГОТОВЫЕ НАБОРЫ ИНГРЕДИЕНТОВ =
    public static final List<String> FULL_BURGER_INGREDIENTS = Arrays.asList(
            FLUORESCENT_BUN_ID,
            PROTOSTOMIA_MEAT_ID,
            SPACE_SAUCE_ID
    );
    /** Бургер только с булкой — минимально валидный набор */
    public static final List<String> BUN_ONLY_INGREDIENTS = List.of(CRATER_BUN_ID);
    /** Пустой список для проверки ошибки 400 */
    public static final List<String> EMPTY_INGREDIENTS = List.of();
    // == НЕВАЛИДНЫЕ ID ИНГРЕДИЕНТОВ ==
    /**
     * Невалидный хеш ингредиента — такой записи нет в базе
     * Сервер вернёт 500 Internal Server Error при попытке создать заказ
     */
    public static final String INVALID_INGREDIENT_ID = "1122334455inv666";

}
