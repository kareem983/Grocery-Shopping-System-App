package com.example.groceryshoppingsystem.Model;

public class favouritesClass {
    public favouritesClass() {
    }

    public favouritesClass(String productimage, String producttitle, String productprice, boolean checked) {
        this.productimage = productimage;
        this.producttitle = producttitle;
        this.productprice = productprice;
        this.checked = checked;
    }

    private String productimage;

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    private String producttitle;
    private String productprice;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private boolean checked;
}