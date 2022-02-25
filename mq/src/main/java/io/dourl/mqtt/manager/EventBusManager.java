package io.dourl.mqtt.manager;

import org.eclipse.paho.android.service.BuildConfig;
import org.greenrobot.eventbus.EventBus;

import io.dourl.mqtt.base.log.LoggerUtil;

/**
 * @author: dou
 * @date: 2022/2/24
 */
public class EventBusManager {

    private EventBus eventBus;

    private static EventBusManager instance;

    public static EventBusManager getInstance() {
        if (instance == null) {
            instance = new EventBusManager();
        }
        return instance;
    }

    private EventBusManager() {
        this.eventBus = EventBus.builder()
                .eventInheritance(false)//不考虑event之间的继承关系
                .throwSubscriberException(BuildConfig.DEBUG)
                .logNoSubscriberMessages(BuildConfig.DEBUG)
                .sendNoSubscriberEvent(false).build();
    }

    public void post(Object object) {
        if (object == null) {
            LoggerUtil.e("object is null!!!");
            return;
        }
        eventBus.post(object);
    }

    public void postSticky(Object object) {
        if (object == null) {
            LoggerUtil.e("object is null!!!");
            return;
        }
        eventBus.postSticky(object);
    }

    public void register(Object subscriber) {
        if (!eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber);
        }
    }

    public void unRegister(Object subscriber) {
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }

    public boolean isRegister(Object subscriber) {
        return eventBus.isRegistered(subscriber);
    }

    public boolean removeStickyEvent(Object event) {
        return eventBus.removeStickyEvent(event);
    }

    public void removeAllStickyEvents() {
        eventBus.removeAllStickyEvents();
    }
}

