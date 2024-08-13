package com.example.navbotdialog.volunteermodule;


public class annouceModelClass {
    String userMsg,date,time ;

    public annouceModelClass() {
    }

    public annouceModelClass(String userMsg, String date, String time) {

        this.userMsg = userMsg;
        this.date = date;
        this.time = time;
    }

    public String getUserMsg() {
        return userMsg;
    }
    public void setUserMsg(String msg) {
        this.userMsg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
