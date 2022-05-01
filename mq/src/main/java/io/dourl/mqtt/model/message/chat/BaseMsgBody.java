package io.dourl.mqtt.model.message.chat;

import android.os.FileUtils;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;


import java.util.ArrayList;
import java.util.List;

import io.dourl.mqtt.base.BaseObject;
import io.dourl.mqtt.manager.GsonManager;

/**
 *
 */
public class BaseMsgBody implements BaseObject {

    /**
     * type : 2
     * content : {"r":"http://im.resource.nihao.com/FuRi-VjwCExEzEs4Dc9rKAx3X0AP","w":1136,"h":754}
     */

    protected BodyType type;
    protected ExtraEntity extra;

    /**
     * 文件的本地路径
     */
    protected String localPath;

    public BodyType getType() {
        return type;
    }

    public void setType(BodyType bodyType) {
        this.type = bodyType;
    }

    public String getEntityBody() {
        return GsonManager.getGson().toJson(this);
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public ExtraEntity getExtra() {
        return extra;
    }

    public void setExtra(ExtraEntity extra) {
        this.extra = extra;
    }


    @Override
    public String toString() {
        return "BaseMsgBody{" +
                "type=" + type +
                ", extra=" + extra +
                ", localPath='" + localPath + '\'' +
                '}';
    }

    /**
     * 返回本地或服务端地址
     *
     * @return
     */
    public String getLocalOrServerPath() {
        if (!TextUtils.isEmpty(localPath)) {
            return localPath;
        }
        return getServerUrl();
    }

    public String getServerPath() {
        return getServerUrl();
    }

    protected String getServerUrl() {
        return null;
    }

    public static BaseMsgBody buildBody(String databaseValue) {
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
        BaseMsgBody baseMsgBody = null;
        if (databaseValue != null) {
            try {
                baseMsgBody = gson.fromJson(databaseValue, BaseMsgBody.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            if (baseMsgBody != null) {
                switch (baseMsgBody.getType()) {
                    case UN_RECOGNIZE:
                        break;
                    case TYPE_SYS_COMMAND:
                        return gson.fromJson(databaseValue, HintBody.class);
                    case TYPE_TEXT:
                        return gson.fromJson(databaseValue, TextBody.class);
                    case TYPE_IMAGE:
                        return gson.fromJson(databaseValue, ImageBody.class);
                    case TYPE_A_IMAGE:
                        return gson.fromJson(databaseValue, AImageBody.class);
                    case TYPE_AUDIO:
                        return gson.fromJson(databaseValue, AudioBody.class);
                    case TYPE_VIDEO:
                        return gson.fromJson(databaseValue, VideoBody.class);
                    case TYPE_LOCATION:
                        break;
                    case TYPE_LARGE_VIDEO:
                        break;
                    case TYPE_CONTACT:
                        break;
                    case TYPE_MEDIA_CARD:
                        break;
                    case TYPE_FANCY_STICKER:
                        break;
                    case TYPE_FANCY_SMILE_BALL:
                        return gson.fromJson(databaseValue, EmptyEmojiBody.class);
                    case TYPE_PUSH_ALERT:
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
                    case TYPE_GROUP_APPLY_NUM:
                    case TYPE_GROUP_CREAT:
                    case TYPE_GROUP_ADD_MANAGER:
                        return gson.fromJson(databaseValue, HintBody.class);
                    case TYPE_RED_PACKET:
                        return gson.fromJson(databaseValue, RedPacketBody.class);
                    case TYPE_RED_PACKET_COLLECT:
                        return gson.fromJson(databaseValue, RedPacketOpenBody.class);
                }
            }
        }
        return baseMsgBody;
    }


    public static JsonElement toJson(BaseMsgBody body) {
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
        switch (body.getType()) {
            case UN_RECOGNIZE:
                break;
            case TYPE_SYS_COMMAND:
                return gson.toJsonTree(body, HintBody.class);
            case TYPE_TEXT:
                return gson.toJsonTree(body, TextBody.class);
            case TYPE_IMAGE:
                return gson.toJsonTree(body, ImageBody.class);
            case TYPE_A_IMAGE:
                return gson.toJsonTree(body, AImageBody.class);
            case TYPE_AUDIO:
                return gson.toJsonTree(body, AudioBody.class);
            case TYPE_VIDEO:
                return gson.toJsonTree(body, VideoBody.class);
            case TYPE_LOCATION:
                break;
            case TYPE_LARGE_VIDEO:
                break;
            case TYPE_CONTACT:
                break;
            case TYPE_MEDIA_CARD:
                break;
            case TYPE_FANCY_STICKER:
                break;
            case TYPE_FANCY_SMILE_BALL:
                return gson.toJsonTree(body, EmptyEmojiBody.class);
            case TYPE_PUSH_ALERT:
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
            case TYPE_GROUP_APPLY_NUM:
            case TYPE_GROUP_CREAT:
            case TYPE_GROUP_ADD_MANAGER:
                return gson.toJsonTree(body, HintBody.class);
            case TYPE_RED_PACKET:
                return gson.toJsonTree(body, RedPacketBody.class);
            case TYPE_RED_PACKET_COLLECT:
                return gson.toJsonTree(body, RedPacketOpenBody.class);
        }
        return null;
    }

    public static class ExtraEntity implements BaseObject {
        public List<UserEntity> uids ; //@
        public UserEntity fromUser; //单个的就够了
        public String clanId;

    }

    public static class UserEntity implements BaseObject {
        public String uid;
        public String name;

        public UserEntity(String uid, String name) {
            this.uid = uid;
            this.name = name;
        }
    }

    /**
     * 后期统一
     * @return
     */
    public String fromUserUid (){
         if ((getExtra() !=null) && (getExtra().fromUser !=null)) {
            return getExtra().fromUser.uid;
         }
        return "";
    }

    public String clanId (){
        if ((getExtra() !=null)) {
            return getExtra().clanId;
        }
        return "";
    }
    public UserEntity getFormUser(){
        return getExtra().fromUser;
    }
}
