package io.dourl.mqtt.job.core;

import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import io.dourl.mqtt.core.ActionListener;
import io.dourl.mqtt.manager.MqttManager;

/**
 * Mqtt取消订阅job
 * 最多重试两次
 */
public class MqttUnSubJob implements Runnable {

    private static final String TAG = "MqttUnSubJob";
    private String topic;

    private ActionListener.Action action;

    public MqttUnSubJob(String topic) {
        this.topic = topic;
    }

    @Override
    public void run() {
        try {
            MqttAndroidClient client = MqttManager.getInstance().getClient();
            IMqttToken token = client.unsubscribe(topic);
            token.waitForCompletion();
            int[] grantQos = token.getGrantedQos();
            if (grantQos != null && grantQos.length > 0) {
                Log.e(TAG, String.format("unsub ack: Topic: %s, GrantQos: %d", token.getTopics()[0], grantQos[0]));
            }
            action = ActionListener.Action.UNSUBSCRIBE;
            //EventBusManager.getInstance().post(new MqttOpSuccessEvent(action));
        } catch (MqttException e) {
            Log.e(TAG, String.format("unsub sub_topic: %s failed!! Because %s", e.getMessage()));
            // EventBusManager.getInstance().post(new MqttOpFailEvent(action));
        }
    }

}
