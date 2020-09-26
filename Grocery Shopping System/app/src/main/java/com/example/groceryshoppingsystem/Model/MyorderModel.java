package com.example.groceryshoppingsystem.Model;

public class MyorderModel {
  private String OrderID, Date, orderNums, orderPrice, orderProducts, OrderCheck;

    public MyorderModel(String OrderId, String date, String orderNums, String orderPrice, String orderProducts, String OrderCheck) {
        this.OrderID = OrderId;
        Date = date;
        this.orderNums = orderNums;
        this.orderPrice = orderPrice;
        this.orderProducts = orderProducts;
        this.OrderCheck = OrderCheck;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOrderNums() {
        return orderNums;
    }

    public void setOrderNums(String orderNums) {
        this.orderNums = orderNums;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(String orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getOrderCheck() {
        return OrderCheck;
    }

    public void setOrderCheck(String orderCheck) {
        OrderCheck = orderCheck;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }
}
