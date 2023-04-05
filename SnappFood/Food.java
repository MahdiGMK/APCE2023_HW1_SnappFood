package SnappFood;

public class Food {
    private final String name;
    private final String category;
    private final int price;
    private final int cost;

    public Food(String name, String category, int price, int cost) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.cost = cost;
    }

    // Getter Setter
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public int getCost() {
        return cost;
    }
// Getter Setter End
}
