package io.dourl.mqtt.manager;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.android.service.BuildConfig;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.Future;

import io.dourl.mqtt.base.MqttBaseApp;
import io.dourl.mqtt.core.ActionListener;
import io.dourl.mqtt.core.MqttCallbackHandler;
import io.dourl.mqtt.core.MqttConstant;
import io.dourl.mqtt.core.MqttTraceCallback;
import io.dourl.mqtt.job.MsgJobManager;
import io.dourl.mqtt.job.core.MqttConAndSubJob;
import io.dourl.mqtt.job.core.MqttDestroyJob;
import io.dourl.mqtt.utils.TopicUtils;

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
            Log.e(TAG, "already init");
            return;
        }
        if (!LoginManager.isLogin()) {
            Log.e(TAG, "user not login");
            return;
        }
        String cId = LoginManager.getCurrentUserId();
        mqttAndroidClient = new MqttAndroidClient(MqttBaseApp.getApp(),
                MqttConstant.URI, cId);
        if (BuildConfig.DEBUG) {
            mqttAndroidClient.setTraceCallback(new MqttTraceCallback());
            mqttAndroidClient.setTraceEnabled(true);
        }
        Log.e(TAG, "mqtt init success");
    }

    public MqttAndroidClient getClient() {
        return mqttAndroidClient;
    }

    /**
     *  Clean Session 标志告诉代理客户端是否要建立持久会话
     *  在持久会话 (CleanSession = false) 中，代理会存储客户端的所有订阅以及以服务质量 (QoS) 级别 1 或 2 订阅的客户端的所有丢失消息。
     *  如果会话不是持久的 (CleanSession = true )，代理不为客户端存储任何内容，并清除任何先前持久会话中的所有信息。
     *
     *  cleanSession标志是MQTT协议中对一个客户端建立TCP连接后是否关心之前状态的定义。具体语义如下：
     * cleanSession=true：客户端再次上线时，将不再关心之前所有的订阅关系以及离线消息。
     * cleanSession=false：客户端再次上线时，还需要处理之前的离线消息，而之前的订阅关系也会持续生效。
     * @param cleanSession
     * @return
     */
    public IMqttToken connect(boolean cleanSession) {
        String userName, password;
        //LoggerUtils.e("deviceid: " + Constants.DEVICEID);
        if (LoginManager.isLogin()) {
            //userName = LoginManager.getToken() + "_2" +" Constants.DEVICEID";
            userName = LoginManager.getInstance().getCurrentUserId();
            //password = "DigestUtils.md5Hex(userName + LoginManager.getSecret())";
            password = LoginManager.getSecret() + LoginManager.getInstance().getCurrentUserId();
        } else {
            return null;
        }
        // NotificationHelper.showNoti(ActionListener.Action.CONNECT, false, "CONNECTING");
        MqttConnectOptions conOpt = new MqttConnectOptions();
       /* X509TrustManager tm = new MqttX509TrustManager();
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{tm}, null);
            conOpt.setSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        conOpt.setConnectionTimeout(MqttConstant.TIMEOUT);
        conOpt.setKeepAliveInterval(MqttConstant.KEEP_ALIVE);
        conOpt.setUserName(userName);
        conOpt.setPassword(password.toCharArray());
        conOpt.setCleanSession(cleanSession);
        mqttAndroidClient.setCallback(new MqttCallbackHandler());
        try {
            return mqttAndroidClient.connect(conOpt, null,
                    new ActionListener((Application) MqttBaseApp.getApp(), ActionListener.Action.CONNECT, null));
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
//
//        public IMqttToken unSubscribe(String topic) {
//        if (TextUtils.isEmpty(topic)) {
//            LoggerUtils.e("sub_topic is null");
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

    public IMqttToken disconnect() {
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
                TopicUtils.getImTopic(), 1, true));
        //测试群
        MsgJobManager.getInstance().addJob(new MqttConAndSubJob(
                TopicUtils.getGimTopic("kamen_qun"), 1, true));
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

    public void clear() throws MqttException {
        unRegisterResource();
        mqttAndroidClient = null;
        ourInstance = null;
    }
}
