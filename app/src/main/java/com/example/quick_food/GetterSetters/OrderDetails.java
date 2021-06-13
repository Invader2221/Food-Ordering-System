package com.example.quick_food.GetterSetters;

public class OrderDetails {

    private String orderId, status, userId, totalCost, totalItems;

    public OrderDetails(String orderId, String status, String userId, String totalCost, String totalItems) {
        this.orderId = orderId;
        this.status = status;
        this.userId = userId;
        this.totalCost = totalCost;
        this.totalItems = totalItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }
}
