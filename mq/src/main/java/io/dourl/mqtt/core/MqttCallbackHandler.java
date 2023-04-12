/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 * <p/>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * <p/>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */
package io.dourl.mqtt.core;

import android.content.Intent;
import android.util.Log;


import io.dourl.mqtt.MQTTApp;
import io.dourl.mqtt.base.MqttBaseApp;
import io.dourl.mqtt.constants.IntentAction;
import io.dourl.mqtt.utils.MessageThreadPool;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.dourl.mqtt.BuildConfig;
import io.dourl.mqtt.event.mqtt.MqttOpFailEvent;
import io.dourl.mqtt.job.msg.ReceiveMsgJob;
import io.dourl.mqtt.manager.EventBusManager;

/**
 * Handles call backs from the MQTT Client
 */
public class MqttCallbackHandler implements MqttCallback {

    public static String TAG = "MqttCallbackHandler";

    /**
     * Creates an <code>MqttCallbackHandler</code> object
     */
    public MqttCallbackHandler() {
    }

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    @Override
    public void connectionLost(Throwable cause) {//cause 为空时，代表主动调用disconnect
        if (cause != null) {
            if (BuildConfig.DEBUG) {
                cause.printStackTrace();
            }
            if (MqttBaseApp.getApp() != null) {
                MqttBaseApp.getApp().sendBroadcast(new Intent(IntentAction.ACTION_WAKE_MQTT));
            }
            EventBusManager.getInstance().post(new MqttOpFailEvent(ActionListener.Action.CONNECT));
        }
       Log.e(TAG,"connectionLost, cause: " + (cause != null ? cause.getMessage() : null));
        NotificationHelper.showNoti(ActionListener.Action.DISCONNECT, false, "CONNECTION LOST");
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG,"meaasgeArrived message:" + message.toString());
        MessageThreadPool.INSTANCE.receiveMessage(new ReceiveMsgJob(topic, message));
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG,String.format("deliveryComplete token is %s", token.toString()));
    }

}
