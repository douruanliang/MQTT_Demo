package io.dourl.mqtt.dao.bean;

import android.os.Parcel;
import android.os.Parcelable;

import android.text.Spannable;

import androidx.annotation.Keep;
import androidx.annotation.WorkerThread;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.JsonAdapter;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;
import org.jetbrains.annotations.NotNull;

import io.dourl.mqtt.R;
import io.dourl.mqtt.base.BaseApp;
import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.model.ClanModel;
import io.dourl.mqtt.model.message.chat.BaseMsgBody;
import io.dourl.mqtt.model.message.chat.BodyType;
import io.dourl.mqtt.model.message.chat.HintBody;
import io.dourl.mqtt.model.message.chat.MessageType;
import io.dourl.mqtt.model.message.chat.RedPacketBody;
import io.dourl.mqtt.model.message.chat.RedPacketOpenBody;
import io.dourl.mqtt.model.message.chat.TextBody;
import io.dourl.mqtt.model.typeadapter.BodyTypeAdapter;
import io.dourl.mqtt.utils.IMTextBodyUtils;
import io.dourl.mqtt.utils.chat.TextBodyContentUtils;


/**
 * 消息
 * 实体消息类字段分布在子类中
 */
@Entity
public class MessageModel implements BaseObject, Parcelable, Cloneable {

    //    private final static Gson GSON = new Gson();
    private final static Gson GSON = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return clazz == Spannable.class;
        }
    }).create();

    public enum Status {
        sending, fail, success
    }

    /**
     * msgId : cc1c6c0f95638ce19e14cc1b4856f1f412c08eab
     * type : 1
     * from : {"id":"10042","fullname":"鹏鹏","sessionIcon":"http://7xo8e9.com2.z0.glb.qiniucdn.com/avatar/2ddeafaeadc29828b9c77ef0e3281abe.jpg","sex":"1","truthful":"0","age":"25","intro":"你好地球"}
     * body : {"type":1,"content":[{"t":"txt","c":"en"}]}
     * time : 1452063976626
     */

    @Id(autoincrement = true)
    protected Long id;

    @Unique
    protected String msgid;

    /**
     * 消息类型，普通消息，通知等等
     * 具体的消息类型，请关注body中的type
     */
    @Convert(converter = MessageTypeConverter.class, columnType = Integer.class)
    protected MessageType type;

    /**
     * 发送者
     */
    @ToOne(joinProperty = "fromUid")
    protected UserModel from;

    /**
     * 发送者的uid
     */
    @NotNull
    protected String fromUid;

    /**
     * 接收者
     */
    @ToOne(joinProperty = "toUid")
    protected UserModel to;

    /**
     * 接收者的uid
     */
    @NotNull
    protected String toUid;

    /**
     * 群聊信息
     */
    @Convert(converter = ClanConverter.class, columnType = String.class)
    protected ClanModel clan;
    /**
     * type : 1
     * content : [{"t":"txt","c":"en"}]
     */

    /**
     * 服务器时间
     */
    @NotNull
    protected long time;

    /**
     * 本地创建消息的时间
     */
    protected long localTime;

    /**
     * 消息是否已读
     */
    protected boolean isRead = false;

    protected String sessionId;

    protected boolean isMine;

    /**
     * 消息状态
     */
    @Convert(converter = StatusConverter.class, columnType = String.class)
    protected Status sendStatus = Status.success;

    protected boolean downloading;

    @Convert(converter = BodyConverter.class, columnType = String.class)
    @JsonAdapter(BodyTypeAdapter.class)
    protected BaseMsgBody body;

    private transient boolean isShowTime;

    public MessageModel() {
    }

    

    @Keep
    public MessageModel(MessageModel messageModel) {
        this.msgid = messageModel.msgid;
        this.type = messageModel.type;
        this.fromUid = messageModel.fromUid;
        this.from = messageModel.getFromUser();
        this.toUid = messageModel.toUid;
        this.to = messageModel.to;
        this.time = messageModel.time;
        this.localTime = messageModel.localTime;
        this.isRead = messageModel.isRead;
        this.sessionId = messageModel.sessionId;
        this.isMine = messageModel.isMine;
        this.sendStatus = messageModel.sendStatus;
        this.downloading = messageModel.downloading;
        this.body = messageModel.body;
    }

    public long getTime() {
        return time;
    }

    public String getMsgId() {
        return msgid;
    }

    public void setMsgId(String msgId) {
        this.msgid = msgId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public long getLocalTime() {
        return time;
    }

    public void setLocalTime(long time) {
        this.time = time;
    }

    public String getEntityBody() {
        if (body != null) {
            return GSON.toJson(body);
        }
        return "";
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * 获取消息的描述
     *
     * @return 文本：文本内容，图片：图片，视频，视频，以此类推
     */
    public CharSequence getContentDesc() {
        String result = "";
        switch (getBodyType()) {
            case TYPE_TEXT:
                Spannable builder;
                builder = TextBodyContentUtils.getSpannableContent(BaseApp.getApp(), ((TextBody) body).getContent());
                return builder.toString();
            case TYPE_IMAGE:
            case TYPE_A_IMAGE:
                result = BaseApp.getApp().getString(R.string.image_content_desc);
                break;
            case TYPE_AUDIO:
                result = BaseApp.getApp().getString(R.string.audio_content_desc);
                break;
            case TYPE_VIDEO:
                result = BaseApp.getApp().getString(R.string.video_content_desc);
                break;
            case TYPE_FANCY_SMILE_BALL:
                result = BaseApp.getApp().getString(R.string.empty_emoji_content_desc);
                break;
            case TYPE_CHAT_REJECT:
            case TYPE_GROUP_ADD_USER:
            case TYPE_GROUP_INVITE_USER:
            case TYPE_GROUP_USER_ADD:
            case TYPE_GROUP_DELETE_USER:
            case TYPE_GROUP_TRANS_OWNER:
            case TYPE_GROUP_UPDATE_ICON:
            case TYPE_GROUP_UPDATE_NAME:
            case TYPE_GROUP_UPDATE_COLOR:
            case TYPE_GROUP_ADD_GAME:
            case TYPE_GROUP_UPDATE_TYPE:
            case TYPE_GROUP_CREAT:
            case TYPE_GROUP_ADD_MANAGER:
            case TYPE_SYS_COMMAND:
                result = IMTextBodyUtils.processHintTxt((HintBody) body);
                break;
            case TYPE_GROUP_APPLY_NUM:
                StringBuilder builder1 = new StringBuilder("");
                for (HintBody.ContentEntity text : ((HintBody) body).getContent()) {
                    builder1.append(text.getC());
                }
                result = builder1.toString();
                break;
            case TYPE_RED_PACKET:
                RedPacketBody.ContentEntity contentEntity = ((RedPacketBody) body).getContent().get(0);
                result = BaseApp.getApp().getString(R.string.red_pkg_content_desc, contentEntity.getCoin_type().toUpperCase(), contentEntity.getC());
                break;

            case TYPE_RED_PACKET_COLLECT:
                result = ((RedPacketOpenBody) body).createSpan(BaseApp.getApp()).toString();
                break;
        }
        return result;
    }

    public Status getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Status sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setIsMine(boolean isMine) {
        this.isMine = isMine;
    }

    @Keep
    public BodyType getBodyType() {
        return body != null ? body.getType() : BodyType.UN_RECOGNIZE;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageModel that = (MessageModel) o;

        return msgid.equals(that.msgid);

    }

    @Override
    public int hashCode() {
        return msgid.hashCode();
    }

    public String getFromUid() {
        return this.fromUid;
    }

    public void setFromUid(String uid) {
        this.fromUid = uid;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public boolean getIsMine() {
        return this.isMine;
    }

    public boolean getDownloading() {
        return this.downloading;
    }

    public BaseMsgBody getBody() {
        return body;
    }

    public void setBody(BaseMsgBody body) {
        this.body = body;
    }

    public String getLocalPath() {
        if (body != null) {
            return body.getLocalPath();
        }
        return "";
    }

    public void setLocalPath(String path) {
        if (body != null) {
            body.setLocalPath(path);
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
/*    @Generated()
    @Keep
    @WorkerThread
    public UserModel getFrom() {
        if (from == null) {
            String __key = this.fromUid;
            if (from__resolvedKey == null || from__resolvedKey != __key) {
                final DaoSession daoSession = this.daoSession;
                if (daoSession == null) {
                    throw new DaoException("Entity is detached from DAO context");
                }
                UserModelDao targetDao = daoSession.getUserModelDao();
                UserModel fromNew = targetDao.load(__key);
                synchronized (this) {
                    from = fromNew;
                    from__resolvedKey = __key;
                }
            }
        }
        return from;
    }*/

    public UserModel getFromUser() {
        return from;
    }



    static class StatusConverter implements PropertyConverter<Status, String> {

        @Override
        public Status convertToEntityProperty(String databaseValue) {
            return Status.valueOf(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(Status entityProperty) {
            return entityProperty.name();
        }
    }

    static class MessageTypeConverter implements PropertyConverter<MessageType, Integer> {

        @Override
        public MessageType convertToEntityProperty(Integer databaseValue) {
            return MessageType.valueOf(databaseValue);
        }

        @Override
        public Integer convertToDatabaseValue(MessageType entityProperty) {
            return entityProperty.value();
        }
    }

    static class BodyConverter implements PropertyConverter<BaseMsgBody, String> {

        @Override
        public BaseMsgBody convertToEntityProperty(String databaseValue) {
            return BaseMsgBody.buildBody(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(BaseMsgBody entityProperty) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return false;
                }

                @Override
                public boolean shouldSkipClass(Class<?> incomingClass) {
                    return incomingClass == Spannable.class;
                }
            });
            Gson gson = gsonBuilder.create();
            String json = "";
            try {
                json = gson.toJson(entityProperty);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }
    }

    static class ClanConverter implements PropertyConverter<ClanModel, String> {

        @Override
        public ClanModel convertToEntityProperty(String databaseValue) {
            return ClanModel.buildClan(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(ClanModel entityProperty) {

            return new Gson().toJson(entityProperty);
        }
    }

    public String getMsgid() {
        return this.msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getToUid() {
        return this.toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public boolean isShowTime() {
        return isShowTime;
    }

    public void setShowTime(boolean showTime) {
        isShowTime = showTime;
    }


    @Override
    public String toString() {
        return "MessageModel{" +
                "id=" + id +
                ", msgid='" + msgid + '\'' +
                ", type=" + type +
                ", from=" + from +
                ", fromUid='" + fromUid + '\'' +
                ", to=" + to +
                ", toUid='" + toUid + '\'' +
                ", time=" + time +
                ", localTime=" + localTime +
                ", isRead=" + isRead +
                ", sessionId='" + sessionId + '\'' +
                ", isMine=" + isMine +
                ", sendStatus=" + sendStatus +
                ", downloading=" + downloading +
                ", body=" + body +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClanModel getClan() {
        return this.clan;
    }

    public void setClan(ClanModel clan) {
        this.clan = clan;
    }

    public void setFrom(UserModel from) {
        this.from = from;
    }

    public void setTo(UserModel to) {
        this.to = to;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    @Override
    public MessageModel clone() throws CloneNotSupportedException {
        MessageModel item = (MessageModel) super.clone();
        BaseMsgBody body = new BaseMsgBody();
        body.setType(item.getBodyType());
        body.setExtra(item.getBody().getExtra());
        body.setLocalPath(item.getLocalPath());
        item.setBody(body);
        return item;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.msgid);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.from, flags);
        dest.writeString(this.fromUid);
        dest.writeParcelable(this.to, flags);
        dest.writeString(this.toUid);
        dest.writeParcelable(this.clan, flags);
        dest.writeLong(this.time);
        dest.writeLong(this.localTime);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeString(this.sessionId);
        dest.writeByte(this.isMine ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sendStatus == null ? -1 : this.sendStatus.ordinal());
        dest.writeByte(this.downloading ? (byte) 1 : (byte) 0);
        dest.writeSerializable(this.body);
    }

    protected MessageModel(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.msgid = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : MessageType.values()[tmpType];
        this.from = in.readParcelable(UserModel.class.getClassLoader());
        this.fromUid = in.readString();
        this.to = in.readParcelable(UserModel.class.getClassLoader());
        this.toUid = in.readString();
        this.clan = in.readParcelable(ClanModel.class.getClassLoader());
        this.time = in.readLong();
        this.localTime = in.readLong();
        this.isRead = in.readByte() != 0;
        this.sessionId = in.readString();
        this.isMine = in.readByte() != 0;
        int tmpSendStatus = in.readInt();
        this.sendStatus = tmpSendStatus == -1 ? null : Status.values()[tmpSendStatus];
        this.downloading = in.readByte() != 0;
        this.body = (BaseMsgBody) in.readSerializable();
    }



    @Generated(hash = 152672021)
    public MessageModel(Long id, String msgid, MessageType type, @NotNull String fromUid, @NotNull String toUid, ClanModel clan, long time, long localTime, boolean isRead, String sessionId,
            boolean isMine, Status sendStatus, boolean downloading, BaseMsgBody body) {
        this.id = id;
        this.msgid = msgid;
        this.type = type;
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.clan = clan;
        this.time = time;
        this.localTime = localTime;
        this.isRead = isRead;
        this.sessionId = sessionId;
        this.isMine = isMine;
        this.sendStatus = sendStatus;
        this.downloading = downloading;
        this.body = body;
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
}


