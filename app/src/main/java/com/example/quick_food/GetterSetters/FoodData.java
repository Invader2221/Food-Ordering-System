package com.example.quick_food.GetterSetters;

public class FoodData {

    private String id;
    private String itemName;
    private String itemDescription;
    private String itemImage;

    public FoodData(String id, String itemName, String itemDescription, String itemImage) {
        this.id = id;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
