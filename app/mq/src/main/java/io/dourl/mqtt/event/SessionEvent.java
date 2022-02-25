package io.dourl.mqtt.event;

import io.dourl.mqtt.dao.bean.SessionModel;

/**
 * @author: dou
 * @date: 2022/2/24
 */
public class SessionEvent {
    public SessionModel sessionModel;

    public SessionEvent() {
    }

    public SessionEvent(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }
}

