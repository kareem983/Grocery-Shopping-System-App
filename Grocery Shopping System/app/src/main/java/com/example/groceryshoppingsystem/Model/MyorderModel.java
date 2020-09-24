package com.example.groceryshoppingsystem.Model;

public class MyorderModel {
  private   String Date , orderNums,orderPrice,orderProducts;

    public MyorderModel(String date, String orderNums, String orderPrice, String orderProducts) {
        Date = date;
        this.orderNums = orderNums;
        this.orderPrice = orderPrice;
        this.orderProducts = orderProducts;
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
}
