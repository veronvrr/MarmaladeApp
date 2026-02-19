package com.example.observer;

public class UiNotification implements Observer {
    @Override
    public void update(String eventMessage) {
        System.out.println("UI показывает уведомление: " + eventMessage);
        // здесь можно интегрировать фронтенд: отправить сообщение в модель/шаблон
    }
}
