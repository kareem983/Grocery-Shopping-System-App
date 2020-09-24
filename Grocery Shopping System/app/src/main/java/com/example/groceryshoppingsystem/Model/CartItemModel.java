package com.example.groceryshoppingsystem.Model;

public class CartItemModel {
    public static final int cart_item = 0  ;
    private  int type;

    //Cart Item
    private String productImage;
    private int coupon;
    private int price;
    private int cuttedprice;
    private int quantity;
    private int offerApplied;
    private int couponApplied;
    private String producttitle;
    private boolean CartItemDelete = false;
    public CartItemModel(int type, String productImage , String producttitle, int coupon, int price, int cuttedprice, int quantity, int offerApplied, int couponApplied ) {
        this.type = type;
        this.productImage = productImage;
        this.coupon = coupon;
        this.price = price;
        this.cuttedprice = cuttedprice;
        this.quantity = quantity;
        this.offerApplied = offerApplied;
        this.couponApplied = couponApplied;
        this.producttitle = producttitle;

    }

    public boolean isCartItemDelete() {
        return CartItemDelete;
    }

    public void setCartItemDelete(boolean cartItemDelete) {
        CartItemDelete = cartItemDelete;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCuttedprice() {
        return cuttedprice;
    }

    public void setCuttedprice(int cuttedprice) {
        this.cuttedprice = cuttedprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOfferApplied() {
        return offerApplied;
    }

    public void setOfferApplied(int offerApplied) {
        this.offerApplied = offerApplied;
    }

    public int getCouponApplied() {
        return couponApplied;
    }

    public void setCouponApplied(int couponApplied) {
        this.couponApplied = couponApplied;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle;
    }


}
