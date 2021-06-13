package com.example.quick_food.GetterSetters;

public class FoodDetails {

    private String foodId;
    private String foodName;
    private String foodPrice;
    private String itemImage;
    private String SizeOne;
    private String SizeTwo;
    private String SizeThree;
    private String AdderOne;
    private String AdderTwo;
    private String AdderThree;


    public FoodDetails(String foodId, String foodName, String foodPrice, String itemImage, String sizeOne, String sizeTwo, String sizeThree, String adderOne, String adderTwo, String adderThree) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.itemImage = itemImage;
        SizeOne = sizeOne;
        SizeTwo = sizeTwo;
        SizeThree = sizeThree;
        AdderOne = adderOne;
        AdderTwo = adderTwo;
        AdderThree = adderThree;
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

    public String getSizeOne() {
        return SizeOne;
    }

    public void setSizeOne(String sizeOne) {
        SizeOne = sizeOne;
    }

    public String getSizeTwo() {
        return SizeTwo;
    }

    public void setSizeTwo(String sizeTwo) {
        SizeTwo = sizeTwo;
    }

    public String getSizeThree() {
        return SizeThree;
    }

    public void setSizeThree(String sizeThree) {
        SizeThree = sizeThree;
    }

    public String getAdderOne() {
        return AdderOne;
    }

    public void setAdderOne(String adderOne) {
        AdderOne = adderOne;
    }

    public String getAdderTwo() {
        return AdderTwo;
    }

    public void setAdderTwo(String adderTwo) {
        AdderTwo = adderTwo;
    }

    public String getAdderThree() {
        return AdderThree;
    }

    public void setAdderThree(String adderThree) {
        AdderThree = adderThree;
    }
}
