package io.dourl.mqtt;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.android.service.BuildConfig;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.dourl.mqtt.base.BaseApp;
import io.dourl.mqtt.job.MsgJobManager;
import io.dourl.mqtt.manager.LoginManager;

/**
 * Mqtt连接管理类
 */
public class MqttManager {
    public static final String TAG = "MqttManager";
    private static MqttManager ourInstance;

    private MqttAndroidClient mqttAndroidClient;

    public static MqttManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new MqttManager();
        }
        return ourInstance;
    }

    private MqttManager() {
    }

    public void init() {
        if (mqttAndroidClient != null) {
             Log.e(TAG,"already init");
            return;
        }
        if (!LoginManager.isLogin()) {
             Log.e(TAG,"user not login");
            return;
        }
        String cId = LoginManager.getInstance().getCurrentUserId();
        mqttAndroidClient = new MqttAndroidClient(BaseApp.getApp(),
                MqttConstant.URI, cId);
        if (BuildConfig.DEBUG) {
            mqttAndroidClient.setTraceCallback(new MqttTraceCallback());
            mqttAndroidClient.setTraceEnabled(true);
        }
         Log.e(TAG,"mqtt init success");
    }

    public MqttAndroidClient getClient() {
        return mqttAndroidClient;
    }

    public IMqttToken connect(boolean cleanSession) {
        String userName, password;
        //NHLog.e("deviceid: " + Constants.DEVICEID);
        if (LoginManager.isLogin()) {
            userName = LoginManager.getToken() + "_" +" Constants.DEVICEID";
            password = "DigestUtils.md5Hex(userName + LoginManager.getSecret())";
        } else {
            return null;
        }
        NotificationHelper.showNoti(ActionListener.Action.CONNECT, false, "CONNECTING");
        MqttConnectOptions conOpt = new MqttConnectOptions();
        X509TrustManager tm = new MqttX509TrustManager();
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{tm}, null);
            conOpt.setSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        conOpt.setConnectionTimeout(MqttConstant.TIMEOUT);
        conOpt.setKeepAliveInterval(MqttConstant.KEEP_ALIVE);
        conOpt.setUserName(userName);
        conOpt.setPassword(password.toCharArray());
        conOpt.setCleanSession(cleanSession);
        mqttAndroidClient.setCallback(new MqttCallbackHandler());
        try {
            return mqttAndroidClient.connect(conOpt, null,
                    new ActionListener((Application) BaseApp.getApp(), ActionListener.Action.CONNECT, null));
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public IMqttToken subscribe(String topic) {
//        return subscribe(topic, 0);
//    }
//
//    public IMqttToken subscribe(String topic, int qos) {
//        return subscribe(topic, qos, null);
//    }
//
//    public IMqttToken subscribe(String topic, int qos, OperationCallback operationCallback) {
//        try {
//            return mqttAndroidClient.subscribe(topic, qos, null, new ActionListener(FApp.getApp(), Action.SUBSCRIBE, operationCallback));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public IMqttToken subscribe(String[] topics, int[] qos, OperationCallback operationCallback) {
//        try {
//            return mqttAndroidClient.subscribe(topics, qos, null, new ActionListener(FApp.getApp(), Action.SUBSCRIBE, operationCallback));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    //    public IMqttToken unSubscribe(String topic) {
//        if (TextUtils.isEmpty(topic)) {
//            NHLog.e("sub_topic is null");
//            return null;
//        }
//        try {
//            return mqttAndroidClient.unsubscribe(topic, null, new ActionListener(FApp.getApp(), Action.UNSUBSCRIBE));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public IMqttToken publish(String topic, String msg, int qos, boolean retained) {
//        try {
//            return mqttAndroidClient.publish(topic, msg.getBytes(), qos, retained, null, new ActionListener(FApp.getApp(), Action.PUBLISH));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
    IMqttToken disconnect() {
        try {
            if (mqttAndroidClient != null) {
                return mqttAndroidClient.disconnect();
            }
        } catch (MqttException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void connectAndSubinJob() {
        MsgJobManager.getInstance().addJob(new MqttConAndSubJob(
                TopicUtils.getImTopic(), 1, false));
        MsgJobManager.getInstance().addJob(new MqttConAndSubJob(
                TopicUtils.getNewsTopic(), 1, false));
//        JobManager.getInstance().addJob(new MqttConAndSubJob(
//                TopicUtils.getDefaultClanTopic(), 1, false));
    }

    public static void wakeMqtt() {
        if (LoginManager.isLogin()) {
            connectAndSubinJob();
        }
    }

    public IMqttToken unSubAll() {
        try {
            String topic = TopicUtils.getImTopic();
            if (mqttAndroidClient != null && isConnect() && !TextUtils.isEmpty(topic)) {
                return mqttAndroidClient.unsubscribe(new String[]{TopicUtils.getImTopic(), TopicUtils.getNewsTopic()});
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isConnect() {
        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }

    public void unRegisterResource() {
        if (mqttAndroidClient != null) {
            mqttAndroidClient.unregisterResources();
        }
    }

//    public void addSubTopic(String topic) {
//        mSubTopicSet.add(topic);
//    }
//
//    public void removeSubTopic(String topic) {
//        mSubTopicSet.remove(topic);
//    }
//
//    public boolean containTopic(String topic) {
//        return mSubTopicSet.containsKey(topic);
//    }

    /**
     * 页面关闭时，调用该方法完成连接断开等清理工作
     */
    public Future<?> destroy() {
        return MsgJobManager.getInstance().addJob(new MqttDestroyJob());
    }

    void clear() throws MqttException {
        unRegisterResource();
        mqttAndroidClient = null;
        ourInstance = null;
    }
}
