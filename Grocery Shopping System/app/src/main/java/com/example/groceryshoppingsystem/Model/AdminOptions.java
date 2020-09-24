package com.example.groceryshoppingsystem.Model;

public class AdminOptions {
    private String OptionName;
    private int OptionResourceId;

    public AdminOptions(String OptionName, int OptionResourceId ){
        this.OptionName=OptionName;
        this.OptionResourceId= OptionResourceId;
    }

    public String getOptionName() {
        return OptionName;
    }

    public int getOptionResourceId() {
        return OptionResourceId;
    }
}
