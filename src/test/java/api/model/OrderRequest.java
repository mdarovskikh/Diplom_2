package api.model;

import java.util.List;

/**
 * Модель запроса для создания заказа - POST /api/orders
 */
public class OrderRequest {
    private List<String> ingredients;
    public OrderRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
