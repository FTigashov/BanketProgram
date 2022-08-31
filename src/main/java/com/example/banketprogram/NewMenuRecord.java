package com.example.banketprogram;

public class NewMenuRecord {
    String dishName, weight, price, count, allWeight, sumPrice;

    public NewMenuRecord(String dishName, String weight, String price, String count, String allWeight, String sumPrice) {
        this.dishName = dishName;
        this.weight = weight;
        this.price = price;
        this.count = count;
        this.allWeight = allWeight;
        this.sumPrice = sumPrice;
    }

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAllWeight() {
        return allWeight;
    }

    public void setAllWeight(String allWeight) {
        this.allWeight = allWeight;
    }
}
