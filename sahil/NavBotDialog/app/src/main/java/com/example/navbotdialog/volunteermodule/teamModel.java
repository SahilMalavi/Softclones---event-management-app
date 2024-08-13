package com.example.navbotdialog.volunteermodule;

public class teamModel {

    int img ;
    String name , number ;

    public teamModel(int img, String name, String number){
        this.name = name;
        this.number = number;
        this.img = img ;

    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}
