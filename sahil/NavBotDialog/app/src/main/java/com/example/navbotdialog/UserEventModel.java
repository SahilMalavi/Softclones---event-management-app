package com.example.navbotdialog;

public class UserEventModel {
    private String eventName;
    private String eventDesc;
    private String imgName;

    public UserEventModel(String eventName, String eventDesc, String imgName) {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.imgName = imgName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
