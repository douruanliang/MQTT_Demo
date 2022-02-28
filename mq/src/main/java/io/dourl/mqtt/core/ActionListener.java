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

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;


/**
 * This Class handles receiving information from the
 * {@link MqttAndroidClient} and updating the {@link Connection} associated with
 * the action
 */
@SuppressWarnings({"JavadocReference", "JavaDoc"})
public class ActionListener implements IMqttActionListener {

    public static final String TAG = "MqttAction";

    /**
     * Actions that can be performed Asynchronously <strong>and</strong> associated with a
     * {@link ActionListener} object
     */
    public enum Action {
        /**
         * Connect Action
         **/
        CONNECT,
        /**
         * Disconnect Action
         **/
        DISCONNECT,
        /**
         * Subscribe Action
         **/
        SUBSCRIBE,
        /**
         * unSubscribe Action
         **/
        UNSUBSCRIBE,
        /**
         * Publish Action
         **/
        PUBLISH
    }

    /**
     * The {@link Action} that is associated with this instance of
     * <code>ActionListener</code>
     **/
    private Action action;
    /**
     * {@link Context} for performing various operations
     **/
    private Application context;

    private OperationCallback operationCallback;

    /**
     * Creates a generic action listener for actions performed form any activity
     *
     * @param context The application context
     * @param action  The action that is being performed
     */
    public ActionListener(Application context, Action action) {
        this.context = context;
        this.action = action;
    }

    /**
     * @param context
     * @param action
     * @param operationCallback
     */
    public ActionListener(Application context, Action action, OperationCallback operationCallback) {
        this.context = context;
        this.action = action;
        this.operationCallback = operationCallback;
    }

    /**
     * The action associated with this listener has been successful.
     *
     * @param asyncActionToken This argument is not used
     */
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        switch (action) {
            case CONNECT:
                connect();
                break;
            case DISCONNECT:
                disconnect();
                break;
            case SUBSCRIBE:
                subscribe();
                break;
            case UNSUBSCRIBE:
                unSubscribe();
                break;
            case PUBLISH:
                publish();
                break;
        }
        if (operationCallback != null) {
            operationCallback.onSuccess(asyncActionToken);
        }
        NotificationHelper.showNoti(action, true, null);
    }

    /**
     * A publish action has been successfully completed, update connection
     * object associated with the client this action belongs to, then notify the
     * user of success
     */
    private void publish() {
        Log.d(TAG,"publish success");
    }

    /**
     * A subscribe action has been successfully completed, update the connection
     * object associated with the client this action belongs to and then notify
     * the user of success
     */
    private void subscribe() {

        Log.d(TAG,"subscribe success");
    }

    private void unSubscribe() {
        Log.d(TAG,"unSubscribe success");
    }

    /**
     * A disconnection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void disconnect() {
        Log.d(TAG,"disconnect success");
    }

    /**
     * A connection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void connect() {

        Log.d(TAG,"connect success");
    }

    /**
     * The action associated with the object was a failure
     *
     * @param token     This argument is not used
     * @param exception The exception which indicates why the action failed
     */
    @Override
    public void onFailure(IMqttToken token, Throwable exception) {
        switch (action) {
            case CONNECT:
                connect(exception);
                break;
            case DISCONNECT:
                disconnect(exception);
                break;
            case SUBSCRIBE:
                subscribe(exception);
                break;
            case UNSUBSCRIBE:
                unSubscribe(exception);
                break;
            case PUBLISH:
                publish(exception);
                break;
        }
        if (operationCallback != null) {
            operationCallback.onFail(token, exception);
        }
        NotificationHelper.showNoti(action, false, null);
    }

    /**
     * A publish action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void publish(Throwable exception) {
    }

    /**
     * A subscribe action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void subscribe(Throwable exception) {
       Log.e(TAG,"subscribe fail e:"
                + (exception != null ? exception.toString() : ""));
    }

    private void unSubscribe(Throwable exception) {
       Log.e(TAG,"unSubscribe fail e:"
                + (exception != null ? exception.toString() : ""));
    }

    /**
     * A disconnect action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void disconnect(Throwable exception) {
        Log.d(TAG,"disconnect fail e: "
                + (exception != null ? exception.toString() : ""));
    }

    /**
     * A connect action was unsuccessful, notify the user and update client history
     *
     * @param exception This argument is not used
     */
    private void connect(Throwable exception) {
        Log.d(TAG,"connect fail e: "
                + (exception != null ? exception.toString() : ""));
    }

}