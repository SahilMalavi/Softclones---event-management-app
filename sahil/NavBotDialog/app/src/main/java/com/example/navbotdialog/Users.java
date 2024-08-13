package com.example.navbotdialog;

public class Users {
    String mobile,name,password;

    public Users(String mobile, String name, String password) {
        this.mobile = mobile;
        this.name = name;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
