package com.example.observer;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String eventMessage) {
        for (Observer o : observers) {
            o.update(eventMessage);
        }
    }
}
