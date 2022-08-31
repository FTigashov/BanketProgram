package com.example.banketprogram;

public class MainMenuRecord {
    String dishName, weight, price;

    public MainMenuRecord(String dishName, String weight, String price) {
        this.dishName = dishName;
        this.weight = weight;
        this.price = price;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
