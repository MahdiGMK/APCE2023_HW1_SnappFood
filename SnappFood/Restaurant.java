package SnappFood;

import java.util.ArrayList;
import java.util.HashMap;

public class Restaurant extends User {
    private String type;
    private ArrayList<Food> foods;
    private HashMap<String, Food> foodIndex;


    public Restaurant(String name, String password, String type) {
        super(name, password);
        this.type = type;
        foods = new ArrayList<Food>();
        foodIndex = new HashMap<String, Food>();
    }

    public void addFood(Food food) {
        foods.add(food);
        foodIndex.put(food.getName(), food);
    }

    public Food getFoodByName(String name) {
        return foodIndex.get(name);
    }

    public void removeFood(Food food) {
        foods.remove(food);
        foodIndex.remove(food.getName(), food);
    }

    // Getter Setter
    public ArrayList<Food> getFoods() {
        return foods;
    }

    public String getType() {
        return type;
    }

    // Getter Setter End
}
