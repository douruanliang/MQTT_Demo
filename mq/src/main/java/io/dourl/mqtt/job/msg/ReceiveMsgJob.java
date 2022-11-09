package io.dourl.mqtt.job.msg;


import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.dourl.mqtt.job.MsgJobManager;
import io.dourl.mqtt.manager.GsonManager;
import io.dourl.mqtt.manager.LoginManager;
import io.dourl.mqtt.model.message.ReceiveMessage;
import io.dourl.mqtt.utils.AESUtil;
import io.dourl.mqtt.utils.TopicUtils;

/**
 * 用来处理Mqtt原始内容解析
 */
public class ReceiveMsgJob extends BaseMessageJob {

    private static final String TAG = "ReceiveMsgJob";
    private String mTopic;
    private String mqttMessage;

    public ReceiveMsgJob(String topic, MqttMessage mqttMessage) {
        this.mTopic = topic;
        this.mqttMessage = new String(mqttMessage.getPayload());
        // this.mqttMessage = new String(mqttMessage.getPayload());
    }

    @Override
    public void run() {
        if (!LoginManager.isLogin()) {
            Log.d(TAG, "not login! do nothing when receive msg");
            return;
        }
//        if (!MqttManager.getInstance().containTopic(mTopic)) {
//             Log.d(TAG,(mTopic + "is not sub by client!!!");
//            return;
//        }
        //String msgString = mqttMessage;
        //类型分类
        if (mTopic.contains("user")||mTopic.contains("group")) {
            processImMessage(AESUtil.INSTANCE.decrypt(mqttMessage,mTopic));
        } else if (TopicUtils.getNewsTopic().equalsIgnoreCase(mTopic)) {
            processNewsMessage(mqttMessage);
        }
    }

    private void processImMessage(String msgString) {
        ReceiveMessage parsedMsg = GsonManager.getGson().fromJson(msgString, ReceiveMessage.class);
        switch (parsedMsg.getType()) {
            case UN_RECOGNIZE:
                break;
            case CHAT_NORMAL:
                MsgJobManager.getInstance().addJob(new ProcessChatMsgJob(msgString));
                break;
            case CHAT_GROUP:
                if (parsedMsg.getFromUid().equals(LoginManager.getCurrentUserId()))
                    break;
                MsgJobManager.getInstance().addJob(new ProcessChatMsgJob(msgString));
                break;
//            case TRANSACTION:
//                break;
//            case ADD_FRIEND:
//                NHJobManager.getInstance().addJob(new ProcessAddFriendMsgJob(msgString));
//                break;
//            case EVENT:
//                NHJobManager.getInstance().addJob(new ProcessEventMsgJob(msgString));
//                break;
            default:
                break;
        }
    }

    private void processNewsMessage(String msgString) {
        // MsgJobManager.getInstance().addJob(new ProcessNewsMsgJob(msgString));
    }

}
