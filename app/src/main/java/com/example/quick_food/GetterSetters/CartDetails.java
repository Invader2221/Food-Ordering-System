package com.example.quick_food.GetterSetters;

public class CartDetails {

    private String foodId;
    private String foodName;
    private String foodPrice;
    private String itemImage;
    private String addersAndSizes;

    public CartDetails(String foodId, String foodName, String foodPrice, String itemImage, String addersAndSizes) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.itemImage = itemImage;
        this.addersAndSizes = addersAndSizes;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getAddersAndSizes() {
        return addersAndSizes;
    }

    public void setAddersAndSizes(String addersAndSizes) {
        this.addersAndSizes = addersAndSizes;
    }
}
