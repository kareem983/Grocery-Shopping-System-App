package com.example.groceryshoppingsystem.Model;

public class CategoryProductInfo  {
    private String ProductImage;
    private String ProductName;
    private String ProductPrice;
    private String ProductExpiryDate;
    private boolean IsFavorite;

    public CategoryProductInfo(){

    }

    public CategoryProductInfo(String ProductImage, String ProductName, String ProductPrice, String ProductExpiryDate,boolean IsFavorite){
        this.ProductImage = ProductImage;
        this.ProductName = ProductName;
        this.ProductPrice = ProductPrice;
        this.ProductExpiryDate = ProductExpiryDate;
        this.IsFavorite = IsFavorite;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductExpiryDate() {
        return ProductExpiryDate;
    }

    public void setProductExpiryDate(String productExpiryDate) {
        ProductExpiryDate = productExpiryDate;
    }

    public boolean getIsFavorite() {
        return IsFavorite;
    }

    public void setFavorite(boolean favorite) {
        IsFavorite = favorite;
    }
}
