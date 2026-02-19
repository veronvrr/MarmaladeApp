package com.example.observer;

public class LogNotification implements Observer {
    @Override
    public void update(String eventMessage) {
        System.out.println("LOG: " + eventMessage);
    }
}
