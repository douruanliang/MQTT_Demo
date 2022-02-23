package io.dourl.mqtt.model.message.chat;

import com.google.gson.annotations.SerializedName;

/**
 * 聊天消息体类型
 */
public enum BodyType {
    @SerializedName("-1")
    UN_RECOGNIZE(-1),
    @SerializedName("0")
    TYPE_SYS_COMMAND(0),
    @SerializedName("1")
    TYPE_TEXT(1),
    @SerializedName("2")
    TYPE_IMAGE(2),
    @SerializedName("3")
    TYPE_A_IMAGE(3),
    @SerializedName("4")
    TYPE_AUDIO(4),
    @SerializedName("5")
    TYPE_VIDEO(5), // 小视频
    @SerializedName("6")
    TYPE_LOCATION(6),
    @SerializedName("7")
    TYPE_LARGE_VIDEO(7), // 大视频
    @SerializedName("8")
    TYPE_CONTACT(8), // 联系人
    @SerializedName("9")
    TYPE_MEDIA_CARD(9), // 多媒体卡片
    @SerializedName("10")
    TYPE_FANCY_STICKER(10), // 动画气球
    @SerializedName("11")
    TYPE_FANCY_SMILE_BALL(11), // 彩蛋笑脸
    @SerializedName("12")
    TYPE_PUSH_ALERT(12),// 帐号推送消息
    @SerializedName("13")
    TYPE_CHAT_REJECT(13),//拒收提示
    @SerializedName("17")
    TYPE_GROUP_ADD_USER(17),//批准入群
    @SerializedName("18")
    TYPE_GROUP_INVITE_USER(18),//邀请入群
    @SerializedName("19")
    TYPE_GROUP_USER_ADD(19),//公开加入
    @SerializedName("20")
    TYPE_GROUP_DELETE_USER(20),//踢出群
    @SerializedName("21")
    TYPE_GROUP_TRANS_OWNER(21),//群主转让
    @SerializedName("22")
    TYPE_GROUP_UPDATE_ICON(22),//群图标修改
    @SerializedName("23")
    TYPE_GROUP_UPDATE_NAME(23),//群名称修改
    @SerializedName("24")
    TYPE_GROUP_UPDATE_COLOR(24),//颜色修改
    @SerializedName("25")
    TYPE_GROUP_ADD_GAME(25),//添加游戏
    @SerializedName("26")
    TYPE_GROUP_UPDATE_TYPE(26),//部落类型修改
    @SerializedName("27")
    TYPE_GROUP_APPLY_NUM(27),//未处理申请数
    @SerializedName("28")
    TYPE_GROUP_CREAT(28),//部落创建成功
    @SerializedName("29")
    TYPE_GROUP_ADD_MANAGER(29),//新增管理员
    @SerializedName("40")
    TYPE_RED_PACKET(40),//红包
    @SerializedName("41")
    TYPE_RED_PACKET_COLLECT(41);//红包已经被领取消息

    private int value;

    BodyType(int i) {
        this.value = i;
    }

    public int value() {
        return this.value;
    }

    public static BodyType valueOf(int i) {
        switch (i) {
            case 0:
                return TYPE_SYS_COMMAND;
            case 1:
                return TYPE_TEXT;
            case 2:
                return TYPE_IMAGE;
            case 3:
                return TYPE_A_IMAGE;
            case 4:
                return TYPE_AUDIO;
            case 5:
                return TYPE_VIDEO;
            case 6:
                return TYPE_LOCATION;
            case 7:
                return TYPE_LARGE_VIDEO;
            case 8:
                return TYPE_CONTACT;
            case 9:
                return TYPE_MEDIA_CARD;
            case 10:
                return TYPE_FANCY_STICKER;
            case 11:
                return TYPE_FANCY_SMILE_BALL;
            case 12:
                return TYPE_PUSH_ALERT;
            case 13:
                return TYPE_CHAT_REJECT;
            case 17:
                return TYPE_GROUP_ADD_USER;
            case 18:
                return TYPE_GROUP_INVITE_USER;
            case 19:
                return TYPE_GROUP_USER_ADD;
            case 20:
                return TYPE_GROUP_DELETE_USER;
            case 21:
                return TYPE_GROUP_TRANS_OWNER;
            case 22:
                return TYPE_GROUP_UPDATE_ICON;
            case 23:
                return TYPE_GROUP_UPDATE_NAME;
            case 24:
                return TYPE_GROUP_UPDATE_COLOR;
            case 25:
                return TYPE_GROUP_ADD_GAME;
            case 26:
                return TYPE_GROUP_UPDATE_TYPE;
            case 27:
                return TYPE_GROUP_APPLY_NUM;
            case 28:
                return TYPE_GROUP_CREAT;
            case 29:
                return TYPE_GROUP_ADD_MANAGER;
            default:
                return UN_RECOGNIZE;
        }

    }
    }
