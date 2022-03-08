package io.dourl.mqtt.event;


import io.dourl.mqtt.bean.MessageModel;

/**
 * Created by SpiritTalk on 17/1/21.
 * 语音消息播放状态
 */

public class AudioPlayStatusEvent {
    public MessageModel mAudioMessage;

    public AudioPlayStatusEvent(MessageModel audioMessage) {
        mAudioMessage = audioMessage;
    }
}
