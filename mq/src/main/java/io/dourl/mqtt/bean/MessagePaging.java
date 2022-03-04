package io.dourl.mqtt.bean;

import java.util.List;

import io.dourl.mqtt.base.BaseObject;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/2/28
 */
public class MessagePaging implements BaseObject {
    public boolean hasMore;
    public long lastMsgDbId;
    public List<MessageModel> list;
}
