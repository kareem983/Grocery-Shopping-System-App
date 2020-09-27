package com.example.groceryshoppingsystem.Model;

public class Offers {
    String describtion;
    String img;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public Offers() {
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public Offers(String describtion, String img) {
        this.describtion = describtion;
        this.img = img;
    }

}