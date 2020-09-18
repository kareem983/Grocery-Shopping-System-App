package com.example.groceryshoppingsystem;

public class HorizontalProductModel  {
    private int productimage;
    private String producttitle;
    private String productdesc;
    private String productprice ;

    public HorizontalProductModel(int productimage, String producttitle, String productdesc, String productprice) {
        this.productimage = productimage;
        this.producttitle = producttitle;
        this.productdesc = productdesc;
        this.productprice = productprice;
    }

    public int getProductimage() {
        return productimage;
    }

    public void setProductimage(int productimage) {
        this.productimage = productimage;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public void setProductdesc(String productdesc) {
        this.productdesc = productdesc;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }


}
