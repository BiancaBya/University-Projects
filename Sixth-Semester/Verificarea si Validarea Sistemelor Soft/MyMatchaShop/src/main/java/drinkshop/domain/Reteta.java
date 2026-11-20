package drinkshop.domain;

import java.util.ArrayList;
import java.util.List;

public class Reteta {

    private int productId;
    private List<IngredientReteta> ingrediente;

    public Reteta(int productId, List<IngredientReteta> ingrediente) {
        this.productId = productId;
        this.ingrediente = new ArrayList<>(ingrediente);
    }

    public void setId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }


    public List<IngredientReteta> getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(List<IngredientReteta> ingrediente) {
        this.ingrediente = new ArrayList<>(ingrediente);
    }

    @Override
    public String toString() {
        return "Reteta{" +
                "productId=" + productId +
                ", ingrediente=" + ingrediente +
                '}';
    }
}
