package com.example.navbotdialog;

public class EventModel {
    private String eventName;
    private String eventDesc;
    private String imgName;

    public EventModel()
    {

    }
    public EventModel(String eventName,String eventDesc,String imgName) {
        this.eventName = eventName;
        this.eventDesc = eventDesc;
        this.imgName = imgName;
    }

    public String getEventName() {
        return eventName;
    }


    public String getEventDesc() {

        return eventDesc;
    }

    public String getImgName() {
        return imgName;
    }
}
