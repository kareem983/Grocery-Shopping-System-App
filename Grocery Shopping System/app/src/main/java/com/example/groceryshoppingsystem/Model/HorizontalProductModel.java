package com.example.groceryshoppingsystem.Model;

public class HorizontalProductModel {
    private String productimage;
    private String producttitle;
    private String productprice;
    private boolean checked;
    private String ExpiredDate;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public HorizontalProductModel(String productimage, String producttitle, String productprice,boolean checked, String ExpiredDate) {
        this.productimage = productimage;
        this.producttitle = producttitle;
        this.productprice = productprice;
        this.checked = checked;
        this.ExpiredDate = ExpiredDate;
    }

    public String getExpiredDate() {
        return ExpiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        ExpiredDate = expiredDate;
    }

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


}
