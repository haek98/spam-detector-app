package com.example.asus.spam_one;

public class MessageClass {
    String title,messagePart;
    int id;
    Boolean selectState;

    public MessageClass(String title,String messagePart,Boolean selectState,int id) {
        this.title = title;
        this.messagePart=messagePart;
        this.selectState=selectState;
        this.id=id;
    }
}
