package io.dourl.mqtt.job.core;

import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import io.dourl.mqtt.core.ActionListener;
import io.dourl.mqtt.manager.MqttManager;
import io.dourl.mqtt.manager.LoginManager;

/**
 * Mqtt连接并订阅job
 * 最多重试五次
 * reated by zhangpeng on 15/12/18.
 */
public class MqttConAndSubJob implements Runnable {

    public final String TAG = "MqttConAndSubJob";

    private String topic;

    private int qos;

    private ActionListener.Action action;

    private boolean mCleanSession = true;

    public static final int RETRY_COUNT = 5;

    private int retryCount;

    public MqttConAndSubJob(String topic, int qos, boolean cleanSession) {
        this.topic = topic;
        this.qos = qos;
        mCleanSession = cleanSession;
        retryCount = RETRY_COUNT;
    }

    @Override
    public void run() {
        boolean success = false;
        Exception exception = null;
        for (retryCount=0; retryCount < RETRY_COUNT && !success; retryCount++) {
            try {
                if (!LoginManager.getInstance().isLogin()) {
                    Log.d(TAG,"can not find login info, so abandon doConnect!");
                    break;
                }
                doConnect();
                success = true;
            } catch (Exception e) {
               Log.e(TAG,String.format("mqtt connect fail, retry count is %d", retryCount));
                exception = e;
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                success = false;
                if (e instanceof MqttException &&
                        ((MqttException) e).getReasonCode() == MqttException.REASON_CODE_FAILED_AUTHENTICATION) {
                    break;
                }

            }
        }
        if (!success) {
            if (exception != null) {
               Log.e(TAG,String.format("connect and subscribe sub_topic: %s failed!! reason is %s",
                        topic, exception.toString()));
            }
           // EventBusManager.getInstance().post(new MqttOpFailEvent(action));
        }
    }

    private void doConnect() throws MqttException {
        if (topic == null) return;
        MqttManager.getInstance().init();
        if (!MqttManager.getInstance().isConnect()) {
            Log.d(TAG,"mqtt start connect");
            MqttManager.getInstance().connect(mCleanSession).waitForCompletion(30 * 1000);
            Log.d(TAG,"mqtt connect success");
        } else {
            Log.d(TAG,"mqtt is connect, so only subscribe topic");
        }
        MqttAndroidClient client = MqttManager.getInstance().getClient();
        IMqttToken token;
        action = ActionListener.Action.CONNECT;
        if (!TextUtils.isEmpty(topic)) {
            token = client.subscribe(topic, qos);
            token.waitForCompletion();
            action = ActionListener.Action.SUBSCRIBE;
            Log.d(TAG,String.format("mqtt subscribe topic %s success", topic));
        }
        //EventBusManager.getInstance().post(new MqttOpSuccessEvent(action));
    }

}
