package com.example.groceryshoppingsystem.Model;

public class AdminSalesMan {
    private String name, img , qrimg , salary;

    public AdminSalesMan(String name, String img, String qrimg, String salary) {
        this.name = name;
        this.img = img;
        this.qrimg = qrimg;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQrimg() {
        return qrimg;
    }

    public void setQrimg(String qrimg) {
        this.qrimg = qrimg;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
